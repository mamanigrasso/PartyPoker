package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.model.network.messages.host.InitGameMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.NewCardMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.ShowWinnerMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.UpdateTableMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.WonAmountMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.YourTurnMessage;

public class Game extends Observable {
    private static final Game _instance = new Game();
    private static LinkedList<Player> allPlayers = new LinkedList<>();
    private static int potSize;
    private static int smallBlind;
    private static int roundsBetweenBlindIncrease;
    private static int startChipCount;
    private static int maxPlayers;
    private static int roundCount = 1;
    private static final int FLOP = 0;
    private static final int TURN = 1;
    private static final int RIVER = 2;
    private static ArrayList<Card> communityCards;
    private static int maxBid = 0;
    private static Player currentPlayer;
    private static int stepID = 1;
    private static ModActInterface maInterface;

    private Game() {}

    public static Game getInstance() {

        if (!PartyPokerApplication.isHost())
            throw new IllegalStateException("You're not the host, you fool! You cannot call me!");

        return _instance;
    }

    public void sendInitGameMessage() {
        InitGameMessage initGameMessage = new InitGameMessage();
        initGameMessage.SmallBlind = smallBlind;
        initGameMessage.IsCheatingAllowed = false;
        initGameMessage.PlayerPot = startChipCount;
        initGameMessage.Players = new ArrayList<>();
        initGameMessage.Players.addAll(allPlayers);
        PartyPokerApplication.getMessageHandler().sendMessageToAllClients(initGameMessage);
    }

    public void sendUpdateTableMessage() {
        UpdateTableMessage updateTableMessage = new UpdateTableMessage();
        updateTableMessage.CommunityCards = communityCards;
        updateTableMessage.NewPotSize = potSize;
        updateTableMessage.Players = new ArrayList<>();
        updateTableMessage.Players.addAll(allPlayers);
        PartyPokerApplication.getMessageHandler().sendMessageToAllClients(updateTableMessage);
    }

    public void startRound() {
        System.out.println("***** Round " + roundCount + " *****");

        prepareRound();

        assignBlinds();

        dealOutCards();

        nextStep();
    }

    public void nextStep() {
        ArrayList<Player> players = new ArrayList<>();
        players.addAll(allPlayers);
        maInterface.update(communityCards, potSize, players);
        sendUpdateTableMessage();

        if (areAllPlayersAligned()) {	// are all players aligned on current maxBid (or folded?)
            addPlayerBidsToPot();

            if (isThereAWinner()) {	// if all players have folded except one, we have a winner for this round -> so we start a new round
                ;//startRound();
            } else if (stepID == 3) {   // last step of this round finished
                roundDoneCheckWinner();
            } else {
                maxBid = 0;
                showCommunityCards(stepID++); // either flop, turn or river
                getDealer(); // move dealer to head of queue

                for (Player player : allPlayers) { // all players need to be asked again now
                    player.setCheckStatus(false);
                    player.setStatus("");
                }

                nextStep();
            }
        } else { // not all players aligned yet, so ask next player for action
            currentPlayer = getNextPlayer();

            if (!currentPlayer.isAllIn() && !currentPlayer.hasFolded()) {
                if (currentPlayer.isHost()) {
                    maInterface.showPlayerActions(maxBid-currentPlayer.getCurrentBid());
                }
                else {
                    YourTurnMessage message = new YourTurnMessage();
                    message.MinAmountToRaise = maxBid-currentPlayer.getCurrentBid();
                    PartyPokerApplication.getMessageHandler().sendMessageToDevice(message, currentPlayer.getDevice());
                }
            }
            else {
                nextStep();
            }
        }
    }

    public boolean areAllPlayersAligned() {
        int playersToAct = allPlayers.size();
        int playersFolded = 0;

        for (Player p : allPlayers) {
            if (p.isAllIn() || p.hasFolded() || p.getCheckStatus()) // if player is all in, has folded or has called maxBid
                playersToAct--;

            if (p.hasFolded())
                playersFolded++;
        }

        return playersToAct <= 0 || playersFolded == allPlayers.size()-1;   // if no player can act anymore or all players have folded except one
    }

    public void playerDone() {
        setChanged();
        notifyObservers();
        nextStep();
    }
  
    public void playerBid(int amount) {
        if (amount == 0)
            currentPlayer.setStatus("Checked");
        else if (amount == maxBid)
            currentPlayer.setStatus("Called");
        else if (amount > maxBid) { // player raised, we have a new max to bid
            maxBid = amount;
            currentPlayer.setStatus("Raised");

            for (Player player : allPlayers) { // all other players need to be asked again now
                if (player != currentPlayer && !player.hasFolded()) {
                    player.setCheckStatus(false);
                    player.setStatus("");
                }
            }
        }

        if (currentPlayer.getChipCount() == amount)
            currentPlayer.setAllIn();
        else
            currentPlayer.setChipCount(currentPlayer.getChipCount() - maxBid + currentPlayer.getCurrentBid());

        currentPlayer.setCheckStatus(true);
        currentPlayer.setCurrentBid(amount + currentPlayer.getCurrentBid());

        playerDone();
    }

    public void playerFolded() {
        currentPlayer.setStatus("Folded");
        currentPlayer.setFolded();

        playerDone();
    }

    public void roundDoneCheckWinner() {
        ArrayList<Player> activePlayers = getActivePlayers();
        ArrayList<Player> winners = new ArrayList<Player>();

        if (activePlayers.size() > 1) { // there is still more than one player active, so we need to check hands to figure out the winner(s)
            winners = determineWinner(activePlayers, communityCards);
        } else if (activePlayers.size() == 1) {
            winners.add(activePlayers.get(0));
        } else {	// actually impossible!
            throw new IllegalStateException("No active player anymore!!");
        }

        handleWinner(winners);
    }

    public boolean handleWinner(ArrayList<Player> winners) {
        if (kickOutLosersAndCheckFinalWinner(winners)) // do we have a final winner?
            return true;

        int winAmount = potSize / winners.size(); // in case of split pot

        String winnerInfo = "";

        for (Player winner : winners) {
            String winningHand = getWinningHandString(winner);
            winnerInfo += winner.getName() + " won with " + winningHand + " and got " + winAmount + " chips!\n";
            winner.payOutPot(winAmount);
        }

        ShowWinnerMessage message = new ShowWinnerMessage();
        message.WinnerInfo = winnerInfo;
        PartyPokerApplication.getMessageHandler().sendMessageToAllClients(message);

        maInterface.showWinner(winnerInfo);

        showChipCounts();

        return false;
    }

    public void showChipCounts() {
        int chipCountSum = 0;

        for (Player player : allPlayers) {
            System.out.println("Chip count of " + player.getName() + ": " + player.getChipCount());
            chipCountSum += player.getChipCount();
        }

        System.out.println("Totally they have " + chipCountSum + " chips!");
    }

    public String getWinningHandString(Player winner) {
        ArrayList<Card> cards = new ArrayList<Card>();

        if (communityCards.size() == 5) {	// if all community cards are shown
            cards.addAll(communityCards);
            cards.add(winner.getCard1());
            cards.add(winner.getCard2());

            Card[] cardArray = new Card[cards.size()];
            cards.toArray(cardArray);

            return new Hand(cardArray).display();
        } else
            return winner.getCard1().toString() + " and " + winner.getCard2().toString();
    }

    public int getMaxBid() {
        return maxBid;
    }

    public void addMyObserver(Observer o) {
        addObserver(o);
    }

    /**
     * Initializes the Game instance.
     *
     * @param blind
     *            - small blind amount to start with
     * @param blindIncrease
     *            - specifies number of rounds between blind increase
     * @param chipCount
     *            - chip count every player gets at beginning
     * @param players
     *            - maximum number of allPlayers allowed
     */
    public static void init(int blind, int blindIncrease, int chipCount, int players, ModActInterface maInt) {
        potSize = 0;
        smallBlind = blind;
        roundsBetweenBlindIncrease = blindIncrease;
        startChipCount = chipCount;
        maxPlayers = players;
        allPlayers = new LinkedList<Player>();
        communityCards = new ArrayList<Card>();
        maInterface = maInt;
    }

    /**
     *
     * @param player
     *            - the player instance to add
     * @return true if successful, otherwise false (table full)
     */
    public static boolean addPlayer(Player player) {
        if (allPlayers.size() < maxPlayers) {
            player.setChipCount(startChipCount);
            allPlayers.addFirst(player);
            return true;
        }

        return false; // max player count already reached!
    }

    /**
     *
     * @param player
     *            - the player instance to be removed
     * @return true if successful, otherwise false (only one player left)
     */
    public static boolean removePlayer(Player player) {
        if (allPlayers.size() > 1) {
            if (player.isDealer())
                getNextPlayer().setIsDealer(true);
            allPlayers.remove(player);
            return true;
        }

        return false; // only one player left, cannot be deleted!
    }

    /**
     * prepareRound - reset pot size, prepare players , prepare card deck, ...
     */
    public static void prepareRound() {
        potSize = 0;
        stepID = 0;
        maxBid = 0;
        communityCards.clear();
        preparePlayers();
        assignDealer();
        prepareCardDeck();
        if (roundCount % roundsBetweenBlindIncrease == 0)
            smallBlind *= 2;
        roundCount++;
    }

    /**
     * preparePlayers - removes players cards, resets their blind values and
     * activates all players which still have some chips
     */
    public static void preparePlayers() {
        for (Player player : allPlayers) {
            player.removeCards();

            if (player.getChipCount() > 0)
                player.activate();
        }
    }

    /**
     * getDealer - moves dealer to head of queue
     *
     * @return - returns current dealer
     */
    public static Player getDealer() {
        for (int i = 0; i < allPlayers.size(); i++) {
            Player player = getNextPlayer();

            if (player.isDealer()) {
                return player;
            }
        }

        return allPlayers.get(0);
    }

    /**
     * assignDealer - moves the dealer button to the next player after current
     * dealer
     *
     * @return - the new dealer
     */
    public static Player assignDealer() {
        Player oldDealer = getDealer();
        oldDealer.setIsDealer(false);

        Player newDealer = getNextPlayer();
        newDealer.setIsDealer(true);

        System.out.println(newDealer.getName() + " is dealer now.");

        return newDealer;
    }

    /**
     * prepareCardDeck - fill up card deck, shuffle cards in deck
     */
    public static void prepareCardDeck() {
        CardDeck.fillUp();
        CardDeck.randomizeDeck();
    }

    /**
     * dealOutCards - take one card from card deck and give to first player
     * after dealer, then take next card...
     */
    public static void dealOutCards() {
        for (int i = 1; i <= 2; i++) { // every player should get two cards totally (but one by one)
            for (int j = 0; j < allPlayers.size(); j++) { // first player after dealer gets the first card
                Player player = getNextPlayer();
                Card nextCard = CardDeck.issueNextCardFromDeck();
                player.takeCard(nextCard);

                /*if (!player.isHost()) {
                    NewCardMessage message = new NewCardMessage();
                    message.NewHandCard = nextCard;
                    PartyPokerApplication.getMessageHandler().sendMessageToDevice(message, player.getDevice());
                } else {
                    maInterface.update();
                }*/
            }
        }
    }

    /**
     * getNextPlayer - returns the first player in the queue and moves this
     * player to end of queue
     *
     * @return - the next player in the queue
     */
    public static Player getNextPlayer() {
        Player player = allPlayers.removeFirst();
        allPlayers.addLast(player);
        return player;
    }

    /**
     * assignBlinds - assign small blind to first player after dealer, then big
     * blind
     */
    public static void assignBlinds() {
        Player player = getNextPlayer();
        System.out.println(player.getName() + " is small blind.");
        player.giveBlind(smallBlind);
        player.setCurrentBid(smallBlind);
        player.setChipCount(player.getChipCount() - smallBlind);
        player.setIsSmallBlind(true);

        player = getNextPlayer();
        System.out.println(player.getName() + " is big blind.");
        player.giveBlind(smallBlind * 2);
        player.setCurrentBid(smallBlind*2);
        player.setChipCount(player.getChipCount() - smallBlind * 2);
        player.setIsBigBlind(true);
        maxBid = smallBlind * 2;
    }

    public static ArrayList<Player> getActivePlayers() {
        ArrayList<Player> activePlayers = new ArrayList<Player>();

        for (Player player : allPlayers) {
            if (!player.hasFolded())
                activePlayers.add(player);
        }

        return activePlayers;
    }

    /**
     * addPlayerBidsToPot - after each round the bids of each player are added
     * to the total pot
     */
    public static void addPlayerBidsToPot() {
        for (Player player : allPlayers) {
            potSize += player.getCurrentBid();
            player.resetCurrentBid();
        }

        System.out.println("Current pot size: " + potSize);
    }

    /**
     *
     * @param step
     *            - the current step the dealer does (flop, turn or river)
     */
    public static void showCommunityCards(int step) {
        CardDeck.issueNextCardFromDeck(); // remove one card before dealing
        Card card;

        switch (step) {
            case FLOP:
                System.out.println("Flop cards:");
                for (int i = 0; i < 3; i++) {
                    card = CardDeck.issueNextCardFromDeck();
                    System.out.println(card);
                    communityCards.add(card);
                }
                break;
            case TURN:
                System.out.println("Turn card:");
                card = CardDeck.issueNextCardFromDeck();
                System.out.println(card);
                communityCards.add(card);
                break;
            case RIVER:
                System.out.println("River card:");
                card = CardDeck.issueNextCardFromDeck();
                System.out.println(card);
                communityCards.add(card);
                break;
            default:
                break;
        }
    }

    public boolean isThereAWinner() {
        ArrayList<Player> activePlayers = getActivePlayers();

        if (activePlayers.size() == 1) {	// we have a winner for this round!
            handleWinner(activePlayers);
            return true;
        }

        return false;
    }

    /**
     *
     * @param players
     *            - a list of players which still have their hand
     * @param cards
     *            - the five community cards all players get
     * @return - the player(s) who has(have) the best hand
     */
    public static ArrayList<Player> determineWinner(ArrayList<Player> players, ArrayList<Card> cards) {
        ArrayList<Player> winners = new ArrayList<Player>();

        Hand currHand = null, bestHand = null;

        for (Player player : players) {
            currHand = new Hand(new Card[] { cards.get(0), cards.get(1), cards.get(2), cards.get(3), cards.get(4),
                    player.getCard1(), player.getCard2() });

            System.out.println(player.getName() + " has " + currHand.display());

            if (bestHand == null)
                bestHand = currHand;

            if (currHand.compareTo(bestHand) > 0) {
                winners.clear();
                winners.add(player);
                bestHand = currHand;
            } else if (currHand.compareTo(bestHand) == 0) {
                winners.add(player);
            }
        }

        return winners;
    }

    /**
     *
     * @param winners
     *            - the list of winners
     */
    public static boolean kickOutLosersAndCheckFinalWinner(ArrayList<Player> winners) {
        int playerCount = allPlayers.size();

        for (int i = 0; i < playerCount; i++) {
            Player player = getNextPlayer();

            if (player.isAllIn() && !winners.contains(player)) {
                removePlayer(player);
                System.out.println(player.getName() + " lost and was kicked out!");
            }
        }

        if (allPlayers.size() == 1) {
            System.out.println("***************** We have a final Winner! Congrats to " + allPlayers.get(0).getName() + "!!! *****************");
            return true;
        }

        return false;
    }

    public ArrayList<Card> getCommunityCards() {
        return communityCards;
    }

    // just for testing!
    public void addCommunityCard(Card c) {
        communityCards.add(c);
    }
}