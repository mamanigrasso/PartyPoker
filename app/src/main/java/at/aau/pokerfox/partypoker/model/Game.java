package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;

/**
 * Singleton Game instance.
 */

public class Game {
    private static Game _instance = null;
    private static ArrayList<Player> players;
    private static CardDeck cardDeck;
    private static int potSize;
    private static int smallBlind;
    private static int startChipCount;
    private static int maxPlayers;
    private static int dealerPosition;

    private Game() {}

    public static Game get() {
        if (_instance == null)
            _instance = new Game();

        return _instance;
    }

    public void main() {

        while (true) {

            prepareRound();

            dealOutCards();

            assignBlinds();

            bidRound(smallBlind*2, dealerPosition+3); // start bid round with big blind and first player after big blind
        }
    }

    /**
     * Initializes the Game instance.
     * @param smallBlind - small blind amount to start with
     * @param startChipCount - chip count every player gets at beginning
     * @param maxPlayers - maximum number of players allowed
     */
    public static void init(int smallBlind, int startChipCount, int maxPlayers) {
        potSize = 0;
        dealerPosition = 0;
        smallBlind = smallBlind;
        startChipCount = startChipCount;
        maxPlayers = maxPlayers;
    }

    /**
     *
     * @param player - the player instance to add
     * @return true if successful, otherwise false (table full)
     */
    public boolean addPlayer(Player player) {
        if (players.size() < maxPlayers) {
            players.add(player);
            return true;
        }

        return false; // max player count already reached!
    }

    /**
     *
     * @param player - the player instance to be removed
     * @return true if successful, otherwise false (only one player left)
     */
    public boolean removePlayer(Player player) {
        if (players.size() > 1) {
            players.remove(player);
            return true;
        }

        return false; // only one player left, cannot be deleted!
    }

    /**
     * prepareRound - reset pot size, remove player cards, prepare card deck, ...
     */
    private void prepareRound() {
        this.potSize = 0;
        this.dealerPosition = calculatePlayerPosition(dealerPosition+1);
        removePlayerCards();
        prepareCardDeck();
    }

    /**
     * removePlayerCards - removes all cards from all players
     */
    private void removePlayerCards() {
        for (Player player : players) {
            player.removeCards();
        }
    }

    /**
     * prepareCardDeck - fill up card deck, shuffle cards in deck
     */
    private void prepareCardDeck() {
        cardDeck.fillUp();
        cardDeck.randomizeDeck();
    }

    /**
     * dealOutCards - take one card from card deck and give to first player after dealer, then take next card...
     */
    private void dealOutCards() {
        for (int i=1; i<=2; i++) {  // every player should get two cards totally (but one by one)
            for (int j=dealerPosition+1; j<players.size(); j++) {   // first player after dealer gets the first card
                players.get(j).takeCard(cardDeck.issueNextCardFromDeck());
            }
            for (int j=0; j<=dealerPosition; j++) {   // start from first player in list until dealer position is reached
                players.get(j).takeCard(cardDeck.issueNextCardFromDeck());
            }
        }
    }

    /**
     * assignBlinds - assign small blind to first player after dealer, then big blind
     */
    private void assignBlinds() {
        for (int i=0; i<players.size(); i++) {
            if (players.get(i).isDealer()) {    // dealer found. next player is small blind, then big blind
                dealerPosition = i;
                assignSmallBlind(players.get(calculatePlayerPosition(i+1)));
                assignBigBlind(players.get(calculatePlayerPosition(i+2)));
                break;
            }
        }
    }

    /**
     *
     * @param position - the new desired player position
     * @return calculated position considering list size (make sure we don't run out of list bounds)
     */
    private int calculatePlayerPosition(int position) {
        return position % players.size();
    }

    /**
     *
     * @param player - request small blind amount from player and add up to pot size
     */
    private void assignSmallBlind(Player player) {
        potSize += player.giveBlind(smallBlind);
    }

    /**
     *
     * @param player - request big blind amount from player and add up to pot size
     */
    private void assignBigBlind(Player player) {
        potSize += player.giveBlind(smallBlind*2);    // big blind is twice the value of small blind
    }

    /**
     *
     * @param amount - the amount the bid round should start with
     * @param playerPosition - the player position the round should start with
     */
    private void bidRound(int amount, int playerPosition) {
        int currentBid = amount;
        boolean isRaised = false;
        int activePlayer = calculatePlayerPosition(playerPosition);

        while (true) {  // TO BE CONTINUED!!
            currentBid = players.get(activePlayer).askForAction(currentBid);
        }
    }

    private void showCommunityCards() {

    }
}
