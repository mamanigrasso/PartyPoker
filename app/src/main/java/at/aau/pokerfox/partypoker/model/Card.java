package at.aau.pokerfox.partypoker.model;

import android.os.Parcel;
import android.os.Parcelable;
import at.aau.pokerfox.partypoker.R;

public class Card implements Parcelable {

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
  
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.suit);
        parcel.writeInt(this.rank);
    }

    public static final Parcelable.Creator<Card> CREATOR
            = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    private Card(Parcel in) {
        this.suit = in.readInt();
        this.rank = in.readInt();
    }

    //to get the id of one specific card use: "R.drawable.clubs_4" for example.
    public static int getCards () {

        return CardDeck.getRandomId(CardDeck.addDrawableIds());
    }
}
