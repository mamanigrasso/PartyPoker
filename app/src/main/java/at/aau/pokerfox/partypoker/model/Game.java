package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;


import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.model.network.messages.host.InitGameMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.NewCardMessage;
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

    public void initGame() {
        InitGameMessage initGameMessage = new InitGameMessage();
        initGameMessage.SmallBlind = smallBlind;
        initGameMessage.IsCheatingAllowed = false;
        initGameMessage.PlayerPot = startChipCount;
        initGameMessage.Players = new ArrayList<>();
        initGameMessage.Players.addAll(allPlayers);
        PartyPokerApplication.getMessageHandler().sendMessageToAllClients(initGameMessage);
    }

    public void startRound() {
        System.out.println("***** Round " + roundCount + " *****");

        prepareRound();
        assignBlinds();

        dealOutCards();

        UpdateTableMessage updateTableMessage = new UpdateTableMessage();
        updateTableMessage.CommunityCards = new ArrayList<>();
        updateTableMessage.NewPotSize = 0;
        updateTableMessage.Players = new ArrayList<>();
        updateTableMessage.Players.addAll(allPlayers);
        PartyPokerApplication.getMessageHandler().sendMessageToAllClients(updateTableMessage);

        nextStep();
    }

    public void nextStep() {
        Sleep();
        maInterface.update();
        if (areAllPlayersAligned()) {	// are all players aligned on current maxBid (or folded?)
            addPlayerBidsToPot();

            if (isThereAWinner()) {	// if all players have folded except one, we have a winner for this round -> so we start a new round
                startRound();
            } else if (stepID == 3) {
                if (!roundDoneCheckWinner()) {	// if we have no final winner yet, start a new round, otherwise stop
                    startRound();
                }
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
                if (currentPlayer.getName().compareTo(allPlayers.get(0).getName()) == 0) {    //change this to if (currentPlayer.isHost())
                    if (maxBid == 0)
                        maInterface.showPlayerActions(true);
                    else
                        maInterface.showPlayerActions(false);
                }
                else {
                    maInterface.hidePlayerActions();
//                    currentPlayer.getNewPlayerAction().execute(maxBid);
                    YourTurnMessage message = new YourTurnMessage();
                    message.MinAmountToRaise = Game.getInstance().getMinAmountToRaise();
                    PartyPokerApplication.getMessageHandler().sendMessageToDevice(message, currentPlayer.getDevice());
                }
            }
            else {
                nextStep();
            }
        }
    }

    private boolean areAllPlayersAligned() {
        int playersToAct = allPlayers.size();

        for (Player p : allPlayers) {
            if (p.isAllIn() || p.hasFolded() || p.getCheckStatus()) // if player is all in, has folded or has called maxBid
                playersToAct--;
        }

        return playersToAct <= 0;
    }

    public void playerDone() {
        setChanged();
        notifyObservers();
        nextStep();
    }

    public int getMinAmountToRaise() {
        return smallBlind;
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
        currentPlayer.setCurrentBid(amount);

        playerDone();
    }

    public void playerFolded() {
        currentPlayer.setStatus("Folded");
        currentPlayer.setFolded();

        playerDone();
    }

    private boolean roundDoneCheckWinner() {
        ArrayList<Player> activePlayers = getActivePlayers();
        ArrayList<Player> winners = new ArrayList<Player>();

        maInterface.showAllPlayerCards();

        if (activePlayers.size() > 1) { // there is still more than one player active, so we need to check hands to figure out the winner(s)
            winners = determineWinner(activePlayers, communityCards);
        } else if (activePlayers.size() == 1) {
            winners.add(activePlayers.get(0));
        } else {	// actually impossible!
            throw new IllegalStateException("No active player anymore!!");
        }

        return handleWinner(winners);
    }

    private boolean handleWinner(ArrayList<Player> winners) {
        if (kickOutLosersAndCheckFinalWinner(winners)) // do we have a final winner?
            return true;

            int winAmount = potSize / winners.size(); // in case of split pot


            for (Player player: winners) {
                player.payOutPot(winAmount);
                WonAmountMessage wonAmountMessage = new WonAmountMessage();
                wonAmountMessage.Amount = winAmount;
                PartyPokerApplication.getMessageHandler().sendMessageToDevice(wonAmountMessage, player.getDevice());
            }
        //for (Player winner : winners) {
        //    String winningHand = getWinningHandString(winner);
        //    System.out.println("***** " + winner.getName() + " won with " + winningHand + " and got " + winAmount + " chips! *****");
        //    winner.payOutPot(winAmount);
//        }

        int chipCountSum = 0;

        for (Player player : allPlayers) {
            System.out.println("Chip count of " + player.getName() + ": " + player.getChipCount());
            chipCountSum += player.getChipCount();
        }

//        System.out.println("Totally they have " + chipCountSum + " chips!");

        return false;
    }

    private String getWinningHandString(Player winner) {
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

    public int getPotSize() {
        return potSize;
    }

    /**
     * prepareRound - reset pot size, prepare players , prepare card deck, ...
     */
    private static void prepareRound() {
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
    private static void preparePlayers() {
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
    private static Player getDealer() {
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
    private static Player assignDealer() {
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
    private static void prepareCardDeck() {
        CardDeck.fillUp();
        CardDeck.randomizeDeck();
    }

    /**
     * dealOutCards - take one card from card deck and give to first player
     * after dealer, then take next card...
     */
    private static void dealOutCards() {
        for (int i = 1; i <= 2; i++) { // every player should get two cards totally (but one by one)
            for (int j = 0; j < allPlayers.size(); j++) { // first player after dealer gets the first card
                Player player = getNextPlayer();
                Card nextCard = CardDeck.issueNextCardFromDeck();
                player.takeCard(nextCard);

                NewCardMessage message = new NewCardMessage();
                message.NewHandCard = nextCard;
                PartyPokerApplication.getMessageHandler().sendMessageToDevice(message, player.getDevice());
                maInterface.update();
            }
        }
    }

    /**
     * getNextPlayer - returns the first player in the queue and moves this
     * player to end of queue
     *
     * @return - the next player in the queue
     */
    private static Player getNextPlayer() {
        Player player = allPlayers.removeFirst();
        allPlayers.addLast(player);
        return player;
    }

    /**
     * assignBlinds - assign small blind to first player after dealer, then big
     * blind
     */
    private static void assignBlinds() {
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

    /**
     *
     * @param amount - the amount the bid round should start with
     */
    private static void bidRound(int amount) {
        int maxBid = amount;
        boolean isRaised = true;

        int activePlayerCount = allPlayers.size();	// all players who have not yet folded
        int allInPlayerCount = 0; // all players who are all in

        for (Player player : allPlayers) {
            if (player.hasFolded())
                activePlayerCount--;
            if (player.isAllIn())
                allInPlayerCount++;
        }

        while (isRaised) {
            isRaised = false;   // assume nobody raises, otherwise set flag to true and repeat loop

            for (int j = 0; j<allPlayers.size(); j++) {

                Player player = getNextPlayer();

                if (!player.hasFolded() && !player.isAllIn()) {  // if player is still in hand
                    if (!((activePlayerCount-allInPlayerCount) == 1 && maxBid == player.getCurrentBid())) {	// if only one player is left who has not folded as is not all-in and already called max bid

                        if (player.getName().compareTo(currentPlayer.getName()) == 0)
                            if (amount == 0)
                                maInterface.showPlayerActions(true);
                            else
                                maInterface.showPlayerActions(false);
                        else {
                            maInterface.hidePlayerActions();
//                            player.getNewPlayerAction().execute(amount);
                            YourTurnMessage message = new YourTurnMessage();
                            message.MinAmountToRaise = Game.getInstance().getMinAmountToRaise();
                            PartyPokerApplication.getMessageHandler().sendMessageToDevice(message, currentPlayer.getDevice());
                        }

                        Sleep();

                        if (player.hasFolded()) {	// player has folded
                            activePlayerCount--;

                            if (activePlayerCount == 1)	{ // all other players have folded, so we have a winner!
                                addPlayerBidsToPot();
                                return;
                            }
                        }

                        if (player.isAllIn())	// player is all-in
                            allInPlayerCount++;

                        if (player.getCurrentBid() > maxBid) {   // player has raised
                            isRaised = true;
                            maxBid = player.getCurrentBid(); // current players bid is the new minimum for all other players
                            break;  // restart for loop (all players need to be asked again now)
                        }
                    } else
                        System.out.println("Only one player left - no need to ask anymore!");
                }
            }
        }

        addPlayerBidsToPot();	// bid round finished, now add up all player bids to pot
    }

    private static ArrayList<Player> getActivePlayers() {
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
    private static void addPlayerBidsToPot() {
        for (Player player : allPlayers) {
            potSize += player.getCurrentBid();
            player.resetCurrentBid();
        }

        maInterface.update();
        System.out.println("Current pot size: " + potSize);
        Sleep();
    }

    /**
     *
     * @param step
     *            - the current step the dealer does (flop, turn or river)
     */
    private static void showCommunityCards(int step) {
        CardDeck.issueNextCardFromDeck(); // remove one card before dealing
        Card card;

        switch (step) {
            case FLOP:
                for (int i = 0; i < 3; i++) {
                    card = CardDeck.issueNextCardFromDeck();
                    System.out.println("Community card added: " + card);
                    communityCards.add(card);
                }
                break;
            case TURN:
            case RIVER:
                card = CardDeck.issueNextCardFromDeck();
                System.out.println("Community card added: " + card);
                communityCards.add(card);
                break;
            default:
                break;
        }
    }

    private boolean isThereAWinner() {
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
    private static ArrayList<Player> determineWinner(ArrayList<Player> players, ArrayList<Card> cards) {
        ArrayList<Player> winners = new ArrayList<Player>();

        Hand currHand = null, bestHand = null;

        for (Player player : players) {
            currHand = new Hand(new Card[] { cards.get(0), cards.get(1), cards.get(2), cards.get(3), cards.get(4),
                    player.getCard1(), player.getCard2() });

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
    private static boolean kickOutLosersAndCheckFinalWinner(ArrayList<Player> winners) {
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

    private static void Sleep() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isCurrentPlayerAllIn(int raisedAmount) {
        int chipCount = currentPlayer.getChipCount();

        if (raisedAmount >= chipCount) {
            currentPlayer.setAllIn();
            return true;
        }

        return false;
    }
}