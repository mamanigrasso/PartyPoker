package at.aau.pokerfox.partypoker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.R;

@JsonObject
public class Card implements Parcelable {

    private static String[] suits = { "hearts", "spades", "diamonds", "clubs" };
    private static String[] ranks  = { "Ace", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine", "Ten", "Jack", "Queen", "King" };

    @JsonField
    private int suit;

    @JsonField
    private int rank;
    private int drawableID;

    public Card() {}

    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;

        drawableID = R.drawable.card_back;

        switch (suit) {
            case 0:
                if (rank == 0) drawableID = R.drawable.hearts_ace;
                if (rank == 1) drawableID = R.drawable.hearts_2;
                if (rank == 2) drawableID = R.drawable.hearts_3;
                if (rank == 3) drawableID = R.drawable.hearts_4;
                if (rank == 4) drawableID = R.drawable.hearts_5;
                if (rank == 5) drawableID = R.drawable.hearts_6;
                if (rank == 6) drawableID = R.drawable.hearts_7;
                if (rank == 7) drawableID = R.drawable.hearts_8;
                if (rank == 8) drawableID = R.drawable.hearts_9;
                if (rank == 9) drawableID = R.drawable.hearts_10;
                if (rank == 10) drawableID = R.drawable.hearts_jack;
                if (rank == 11) drawableID = R.drawable.hearts_queen;
                if (rank == 12) drawableID = R.drawable.hearts_king;
                break;

            case 1:
                if (rank == 0) drawableID = R.drawable.spades_ace;
                if (rank == 1) drawableID = R.drawable.spades_2;
                if (rank == 2) drawableID = R.drawable.spades_3;
                if (rank == 3) drawableID = R.drawable.spades_4;
                if (rank == 4) drawableID = R.drawable.spades_5;
                if (rank == 5) drawableID = R.drawable.spades_6;
                if (rank == 6) drawableID = R.drawable.spades_7;
                if (rank == 7) drawableID = R.drawable.spades_8;
                if (rank == 8) drawableID = R.drawable.spades_9;
                if (rank == 9) drawableID = R.drawable.spades_10;
                if (rank == 10) drawableID = R.drawable.spades_jack;
                if (rank == 11) drawableID = R.drawable.spades_queen;
                if (rank == 12) drawableID = R.drawable.spades_king;
                break;

            case 2:
                if (rank == 0) drawableID = R.drawable.diamonds_ace;
                if (rank == 1) drawableID = R.drawable.diamonds_2;
                if (rank == 2) drawableID = R.drawable.diamonds_3;
                if (rank == 3) drawableID = R.drawable.diamonds_4;
                if (rank == 4) drawableID = R.drawable.diamonds_5;
                if (rank == 5) drawableID = R.drawable.diamonds_6;
                if (rank == 6) drawableID = R.drawable.diamonds_7;
                if (rank == 7) drawableID = R.drawable.diamonds_8;
                if (rank == 8) drawableID = R.drawable.diamonds_9;
                if (rank == 9) drawableID = R.drawable.diamonds_10;
                if (rank == 10) drawableID = R.drawable.diamonds_jack;
                if (rank == 11) drawableID = R.drawable.diamonds_queen;
                if (rank == 12) drawableID = R.drawable.diamonds_king;
                break;

            case 3:
                if (rank == 0) drawableID = R.drawable.clubs_ace;
                if (rank == 1) drawableID = R.drawable.clubs_2;
                if (rank == 2) drawableID = R.drawable.clubs_3;
                if (rank == 3) drawableID = R.drawable.clubs_4;
                if (rank == 4) drawableID = R.drawable.clubs_5;
                if (rank == 5) drawableID = R.drawable.clubs_6;
                if (rank == 6) drawableID = R.drawable.clubs_7;
                if (rank == 7) drawableID = R.drawable.clubs_8;
                if (rank == 8) drawableID = R.drawable.clubs_9;
                if (rank == 9) drawableID = R.drawable.clubs_10;
                if (rank == 10) drawableID = R.drawable.clubs_jack;
                if (rank == 11) drawableID = R.drawable.clubs_queen;
                if (rank == 12) drawableID = R.drawable.clubs_king;
                break;
        }

        if (drawableID == R.drawable.card_back)
            System.out.println("invalid card!! Rank: " + rank + " suit: " + suit);
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
        return "*** CARD " + suits[suit] + " " + ranks[rank] + " ***";
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

    public int getDrawableID() {
        return drawableID;
    }
}
