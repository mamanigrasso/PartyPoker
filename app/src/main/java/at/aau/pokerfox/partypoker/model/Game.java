package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class Game extends Observable{
    private static final Game _instance = new Game();
    private static LinkedList<Player> allPlayers;
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
    private static ModActInterface modActInterface;
    private static int stepID = 1;

    public void startRound() {
        System.out.println("Round " + roundCount);

        prepareRound();
        assignBlinds();
        dealOutCards();

        currentPlayer = getNextPlayer();
        askNextPlayer(currentPlayer, smallBlind*2);
    }

    public void askNextPlayer(Player p, int bid) {
        System.out.println("askNextPlayer: " + p.getName() + " " + bid);
        if (areAllPlayersAligned(bid)) {
            if (isThereAWinner())
                startRound();
            else if (stepID == 3){
                roundDoneCheckWinner();
                startRound();
            }
            else {
                addPlayerBidsToPot();
                maxBid = 0;
                showCommunityCards(stepID++);    // either flop, turn or river
                getDealer();    // move dealer to head of queue

                for (Player player : allPlayers) {
                    player.setCheckStatus(false);
                }

                currentPlayer = getNextPlayer();
                askNextPlayer(currentPlayer, 0);
            }
        }
        else {
            if (!p.isAllIn() && !p.hasFolded()) {
                p.setCheckStatus(true);

                if (p.getName().compareTo("Player1") == 0)
                    if (bid == 0)
                        modActInterface.showPlayerActions(true);
                    else
                        modActInterface.showPlayerActions(false);
                else {
                    modActInterface.hidePlayerActions();
                    p.getNewPlayerAction().execute(bid);
                }
            }
            else {
                currentPlayer = getNextPlayer();
                askNextPlayer(currentPlayer, bid);
            }
        }
    }

    private boolean areAllPlayersAligned(int bid) {
        int playersToAct = allPlayers.size();

        for (Player p : allPlayers) {
            if (p.isAllIn() || p.hasFolded())
                playersToAct--;
            else if (p.getCurrentBid() == bid && p.getCheckStatus())
                playersToAct--;
        }

        return playersToAct == 0;
    }

    public void playerDone() {
        setChanged();
        notifyObservers();
        currentPlayer = getNextPlayer();
        askNextPlayer(currentPlayer, maxBid);
    }

    public void playerBid(int amount, boolean isAllIn) {
        if (amount == 0)
            currentPlayer.setCheckStatus(true);

        if (amount > maxBid)    // player raised
            maxBid = amount;

        currentPlayer.setCurrentBid(amount);

        if (isAllIn)
            currentPlayer.setAllIn();
        else
            currentPlayer.setChipCount(currentPlayer.getChipCount() - amount);

        playerDone();
    }

    public void playerFolded() {
        currentPlayer.setFolded();
        playerDone();
    }

    private void roundDoneCheckWinner() {
        ArrayList<Player> activePlayers = getActivePlayers();

        if (activePlayers.size() > 1) {   // there is still more than one player active, so we need to check hands to figure out the winner(s)
            ArrayList<Player> winners = determineWinner(activePlayers, communityCards);
            System.out.println("------------------- Winner is unknown currently -> determineWinner needs to be implemented!  -------------------");

            winners.add(allPlayers.getFirst());	// simulate any player as winner

            kickOutLosersAndCheckFinalWinner(winners);

            int win = potSize/winners.size();	// in case of split pot

            for (Player player: winners) {
                player.payOutPot(win);
            }
        }

        int chipCountSum = 0;

        for (Player player : allPlayers) {
            System.out.println("Chip count of " + player.getName() + ": " + player.getChipCount());
            chipCountSum += player.getChipCount();
        }
    }

    public void startGame() {

        while (allPlayers.size() > 1) {

            System.out.println("__________________________________________ ROUND " + roundCount + "  __________________________________________");

            prepareRound();
            Sleep();
            assignBlinds();
            Sleep();
            dealOutCards();
            Sleep();
            bidRound(smallBlind*2); // start bid round with big blind to call
            Sleep();
            System.out.println("Current pot size: " + potSize);

            if (!isThereAWinner()) {
                for (int i=FLOP; i<=RIVER; i++) {
                    if (i == FLOP)
                        System.out.println("_____________ FLOP _____________");
                    if (i == TURN)
                        System.out.println("_____________ TURN _____________");
                    if (i == RIVER)
                        System.out.println("_____________ RIVER _____________");

                    showCommunityCards(i);	// either flop, turn or river
                    Sleep();
                    getDealer();	// move dealer to head of queue
                    bidRound(0);   // start bid round with first player after dealer
                    Sleep();

                    System.out.println("Current pot size: " + potSize);

                    if (isThereAWinner())
                        break;
                }
            }

            ArrayList<Player> activePlayers = getActivePlayers();

            if (activePlayers.size() > 1) {   // there is still more than one player active, so we need to check hands to figure out the winner(s)
                ArrayList<Player> winners = determineWinner(activePlayers, communityCards);
                System.out.println("------------------- Winner is unknown currently -> determineWinner needs to be implemented!  -------------------");

                winners.add(allPlayers.getFirst());	// simulate any player as winner

                kickOutLosersAndCheckFinalWinner(winners);

                int win = potSize/winners.size();	// in case of split pot

                for (Player player: winners) {
                    player.payOutPot(win);
                }
            }

            int chipCountSum = 0;

            for (Player player : allPlayers) {
                System.out.println("Chip count of " + player.getName() + ": " + player.getChipCount());
                chipCountSum += player.getChipCount();
            }

            System.out.println("Totally they have " + chipCountSum + " chips!");
            Sleep();
        }
    }

    public void addMyObserver(Observer o) {
        addObserver(o);
    }

    /**
     * Initializes the Game instance.
     * @param blind - small blind amount to start with
     * @param blindIncrease - specifies number of rounds between blind increase
     * @param chipCount - chip count every player gets at beginning
     * @param players - maximum number of allPlayers allowed
     */
    public static void init(int blind, int blindIncrease, int chipCount, int players, ModActInterface modAct) {
        potSize = 0;
        smallBlind = blind;
        roundsBetweenBlindIncrease = blindIncrease;
        startChipCount = chipCount;
        maxPlayers = players;
        allPlayers = new LinkedList<Player>();
        communityCards = new ArrayList<Card>();
        modActInterface = modAct;
    }

    public static Game getInstance() {
        return _instance;
    }

    /**
     *
     * @param player - the player instance to add
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
     * @param player - the player instance to be removed
     * @return true if successful, otherwise false (only one player left)
     */
    public static boolean removePlayer(Player player) {
        if (allPlayers.size() > 1) {
            if (player.isDealer())
                getNextPlayer().setDealer(true);
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
        preparePlayers();
        assignDealer();
        prepareCardDeck();
        if (roundCount%roundsBetweenBlindIncrease == 0)
            smallBlind *= 2;
        roundCount++;
    }

    /**
     * preparePlayers - removes players cards, resets their blind values and activates all players which still have some chips
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
     * @return - returns current dealer
     */
    private static Player getDealer() {
        for (int i=0; i<allPlayers.size(); i++) {
            Player player = getNextPlayer();

            if (player.isDealer()) {
                return player;
            }
        }

        return allPlayers.get(0);
    }

    /**
     * assignDealer - moves the dealer button to the next player after current dealer
     * @return - the new dealer
     */
    private static Player assignDealer() {
        Player oldDealer = getDealer();
        oldDealer.setDealer(false);

        Player newDealer = getNextPlayer();
        newDealer.setDealer(true);

        System.out.println(newDealer.getName() + " is dealer.");

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
     * dealOutCards - take one card from card deck and give to first player after dealer, then take next card...
     */
    private static void dealOutCards() {
        for (int i=1; i<=2; i++) {  // every player should get two cards totally (but one by one)
            for (int j = 0; j< allPlayers.size(); j++) {   // first player after dealer gets the first card
                Player player = getNextPlayer();
                player.takeCard(CardDeck.issueNextCardFromDeck());
            }
        }
    }

    /**
     * getNextPlayer - returns the first player in the queue and moves this player to end of queue
     * @return - the next player in the queue
     */
    private static Player getNextPlayer() {
        Player player = allPlayers.removeFirst();
        allPlayers.addLast(player);
        return player;
    }

    /**
     * assignBlinds - assign small blind to first player after dealer, then big blind
     */
    private static void assignBlinds() {
        Player player = getNextPlayer();
        System.out.println(player.getName() + " is small blind.");
        player.giveBlind(smallBlind);

        player = getNextPlayer();
        System.out.println(player.getName() + " is big blind.");
        player.giveBlind(smallBlind*2);
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

                        if (player.getName().compareTo("Player1") == 0)
                            if (amount == 0)
                                modActInterface.showPlayerActions(true);
                            else
                                modActInterface.showPlayerActions(false);
                        else {
                            modActInterface.hidePlayerActions();
                            player.getNewPlayerAction().execute(amount);
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
     * addPlayerBidsToPot - after each round the bids of each player are added to the total pot
     */
    private static void addPlayerBidsToPot() {
        for (Player player : allPlayers) {
            potSize += player.getCurrentBid();
            player.resetCurrentBid();
        }
    }

    /**
     *
     * @param step - the current step the dealer does (flop, turn or river)
     */
    private static void showCommunityCards(int step) {
        CardDeck.issueNextCardFromDeck();   // remove one card before dealing
        Card card;

        switch (step) {
            case FLOP:
                for (int i=0; i<3; i++) {
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

    private static boolean isThereAWinner() {
        ArrayList<Player>activePlayers = getActivePlayers();

        if (activePlayers.size() == 1) {    // we have a winner for this round!
            activePlayers.get(0).payOutPot(potSize);
            System.out.println("------------------- Winner is " + activePlayers.get(0).getName() + ". He totally got " + potSize + " chips!! -------------------");
            return true;
        }

        return false;
    }

    /**
     *
     * @param players - a list of players which still have their hand
     * @param cards - the five community cards all players get
     * @return - the player(s) who has(have) the best hand
     */
    private static ArrayList<Player> determineWinner(ArrayList<Player> players, ArrayList<Card> cards) {
        return new ArrayList<Player>();
    }

    /**
     *
     * @param winners - the list of winners
     */
    private static void kickOutLosersAndCheckFinalWinner(ArrayList<Player> winners) {
        int playerCount = allPlayers.size();

        for (int i=0; i<playerCount; i++) {
            Player player = getNextPlayer();

            if (player.isAllIn() && !winners.contains(player)) {
                removePlayer(player);
                System.out.println(player.getName() + " lost and was kicked out!");
            }
        }

        if (allPlayers.size() == 1)
            System.out.println("We have a Winner! Congrats to " + allPlayers.get(0).getName() + "!!!");
    }

    private static void Sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}