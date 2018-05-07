package at.aau.pokerfox.partypoker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.os.AsyncTask;

import com.peak.salut.SalutDevice;

public class Player implements Parcelable {
    private String name;
    private boolean isAllIn = false;
    private boolean hasFolded = false;
    private boolean isDealer = false;
    private boolean isSmallBlind = false;
    private boolean isBigBlind = false;
    private int chipCount;
    private int currentBid;
    private ArrayList<Card> cards = new ArrayList<Card>();;
    private boolean cheatStatus = false;
    private boolean checkStatus = false;
    private String status = "";
    private SalutDevice device;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getChipCount() {
        return chipCount;
    }

    public void setChipCount(int amount) {
        chipCount = amount;
    }

    public void takeCard(Card card) {
        cards.add(card);
        System.out.println(name + " got " + card.toString());
    }

    public void removeCards() {
        cards.clear();
    }

    public int getCurrentBid() { return currentBid; }

    public void resetCurrentBid() { currentBid = 0; }

    public void setCurrentBid(int bid) { currentBid = bid; }

    public int giveBlind(int blind) {
        if (chipCount < blind) {
            currentBid = chipCount;
            setAllIn();
        }
        else {
            currentBid = blind;
        }

        System.out.println(name + " gave blind " + currentBid);
        return currentBid; // return amount is either the required blind or the whole chipCount if it's less than the blind
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
        isSmallBlind = false;
        isBigBlind = false;
    }

    public void setCheckStatus(boolean status) {
        checkStatus = status;
    }

    public boolean getCheckStatus() {
        return checkStatus;
    }

    public PlayerAction getNewPlayerAction() {
        return new PlayerAction(this);
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
        status = "ALL-IN";
        System.out.println(name + " is ALL-IN!!!");
    }

    public void setFolded() {
        hasFolded = true;
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
        parcel.writeByte((byte) (this.isSmallBlind ? 1 : 0));
        parcel.writeByte((byte) (this.isBigBlind ? 1 : 0));
        parcel.writeInt(this.chipCount);
        parcel.writeInt(this.currentBid);
        parcel.writeTypedList(this.cards);
        parcel.writeByte((byte) (this.cheatStatus ? 1 : 0));
        parcel.writeByte((byte) (this.checkStatus ? 1 : 0));
        parcel.writeString(this.status);
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
        this.isSmallBlind = in.readByte() != 0;
        this.isBigBlind = in.readByte() != 0;
        this.chipCount = in.readInt();
        this.currentBid = in.readInt();
        this.cards = in.createTypedArrayList(Card.CREATOR);
        this.cheatStatus = in.readByte() != 0;
        this.cheatStatus = in.readByte() != 0;
        this.status = in.readString();
    }

    public boolean isBigBlind() {
        return isBigBlind;
    }

    public void setIsBigBlind(boolean isBigBlind) {
        this.isBigBlind = isBigBlind;
    }

    public boolean isSmallBlind() {
        return isSmallBlind;
    }

    public void setIsSmallBlind(boolean isSmallBlind) {
        this.isSmallBlind = isSmallBlind;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDevice(SalutDevice device) {
        this.device = device;
    }

    public SalutDevice getDevice() {
        return device;
    }
}