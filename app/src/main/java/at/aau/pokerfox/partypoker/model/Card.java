package at.aau.pokerfox.partypoker.model;

public class Card {

    private static String[] suits = { "hearts", "spades", "diamonds", "clubs" };
    private static String[] ranks  = { "Ace", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine", "Ten", "Jack", "Queen", "King" };

    private int suit;
    private int rank;

    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public static String[] getSuits() {
        return suits;
    }

    public static void setSuits(String[] suits) {
        Card.suits = suits;
    }

    public static String[] getRanks() {
        return ranks;
    }

    public static void setRanks(String[] ranks) {
        Card.ranks = ranks;
    }

    public int getSuit() {
        return suit;
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String toString() {
        return "*** CARD " + suit + " -> " + rank + " ***";
    }

    public static String rankAsString(int rank ) {
        return ranks[rank];
    }
}
