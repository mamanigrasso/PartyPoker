package at.aau.pokerfox.partypoker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Player implements Parcelable {
    private String name;
    private boolean isAllIn = false;
    private boolean hasFolded = false;
    private boolean isDealer = false;
    private int chipCount;
    private int currentBid;
    private ArrayList<Card> cards;
    private boolean cheatStatus = false;

    public Player(String name) {
        this.name = name;
        cards = new ArrayList<Card>();
    }

    public String getName() {
        return name;
    }

    public int getChipCount() {
        return chipCount;
    }

    public void setChipCount(int startChips) {
        chipCount = startChips;
    }

    public void takeCard(Card card) {
        System.out.println(name + " got " + card.toString());
    }

    public void removeCards() {
        cards.clear();
    }

    public int getCurrentBid() { return currentBid; }

    public void resetCurrentBid() { currentBid = 0; }

    public int giveBlind(int blind) {
        int returnAmount = blind;

        if (chipCount < blind) {
            returnAmount = chipCount;
            currentBid = chipCount;
            setAllIn();
        }
        else {
            chipCount -= blind;
            currentBid = blind;
        }
        System.out.println(name + " gave blind " + returnAmount);
        return returnAmount; // return amount is either the required blind or the whole chipCount if it's less than the blind
    }

    public int askForAction(int amount) {
        int returnAmount = amount;

        if (Math.random()*3 < Math.random()) {
            if (amount == 0) {
                if (chipCount > 60) {
                    returnAmount = 60;
                    System.out.println(name + " bet: " + returnAmount);
                } else {
                    returnAmount = chipCount + currentBid;
                    setAllIn();
                    System.out.println(name + " bet: " + returnAmount);
                }
            } else {
                if (chipCount > returnAmount * 2) {
                    returnAmount = amount * 2;
                    System.out.println(name + " raised: " + returnAmount);
                } else {
                    if (chipCount > 0) {
                        returnAmount = chipCount + currentBid;
                        setAllIn();
                        System.out.println(name + " raised: " + returnAmount);
                    } else
                        returnAmount = 0;
                }
            }
        }

        else if (Math.random()/2 > Math.random()) {
            returnAmount = currentBid;
            hasFolded = true;
            System.out.println(name + " folded");
        } else {
            if (amount == 0)
                System.out.println(name + " checked");
            else {
                if (chipCount > amount)
                    returnAmount = amount;
                else {
                    returnAmount = chipCount + currentBid;
                    setAllIn();
                }
                System.out.println(name + " called: " + returnAmount);

            }
        }

        if (!isAllIn) {
            chipCount -= returnAmount;
            chipCount += currentBid;
        }

        currentBid = returnAmount;

        return returnAmount; // should be the amount specified by the player
    }

    public boolean hasFolded() {
        return hasFolded;
    }

    public boolean isAllIn() {
        return isAllIn;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean isDealer) {
        this.isDealer = isDealer;
    }

    public void payOutPot(int pot) {
        System.out.println(name + " just got " + pot + " chips!");
        chipCount += pot;
    }

    public ArrayList<Card> getPlayerHand() {
        return cards;
    }

    public void activate() {
        hasFolded = false;
        isAllIn = false;
    }

    // Cheating-Area: Player1 loses Chips, if his cheating was blown by player2;
    // Or Player2 loses Chips, because he thought player1 was cheating, but he didn´t;
    public void reduceChipCount(int amountOfReduce) {
        chipCount-=amountOfReduce;
    }

    // Cheating-Area: Opposite to @reduceChipCount;
    // The lost chips of one player go to his opposite
    public void raiseChipCount(int amountOfRaise) {
        chipCount+=amountOfRaise;
    }

    //Cheating-Area: Set @true, if the player has Cheated in the last round;
    public void setCheatStaus(boolean hasCheated) {
        this.cheatStatus=hasCheated;
    }

    //Cheating-Area: Gets the status, if the player has Cheated in the last round;
    public boolean getCheatStatus() {
        return cheatStatus;
    }
  
    public void setAllIn() {
        chipCount = 0;
        isAllIn = true;
        System.out.println(name + " is ALL-IN!!!");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeByte((byte) (this.isAllIn ? 1 : 0));
        parcel.writeByte((byte) (this.hasFolded ? 1 : 0));
        parcel.writeByte((byte) (this.isDealer ? 1 : 0));
        parcel.writeInt(this.chipCount);
        parcel.writeInt(this.currentBid);
        parcel.writeTypedList(this.cards);
        parcel.writeByte((byte) (this.cheatStatus ? 1 : 0));
    }

    public static final Parcelable.Creator<Player> CREATOR
            = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    private Player(Parcel in) {
        this.name = in.readString();
        this.isAllIn = in.readByte() != 0;
        this.hasFolded = in.readByte() != 0;
        this.isDealer = in.readByte() != 0;
        this.chipCount = in.readInt();
        this.currentBid = in.readInt();
        this.cards = in.createTypedArrayList(Card.CREATOR);
        this.cheatStatus = in.readByte() != 0;
    }
}