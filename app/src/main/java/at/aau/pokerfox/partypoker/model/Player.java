package at.aau.pokerfox.partypoker.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.peak.salut.SalutDevice;


@JsonObject
public class Player implements Parcelable {
    @JsonField
    private String name;
    @JsonField
    private boolean isAllIn = false;
    @JsonField
    private boolean hasFolded = false;
    @JsonField
    private boolean isDealer = false;
    @JsonField
    private boolean isSmallBlind = false;
    @JsonField
    private boolean isBigBlind = false;
    @JsonField
    private int chipCount;
    @JsonField
    private int currentBid;
    @JsonField
    private ArrayList<Card> cards = new ArrayList<Card>();
    @JsonField
    private boolean cheatStatus = false;
    @JsonField
    private boolean checkStatus = false;
    @JsonField
    private String status = "";
    @JsonField
    private String deviceId;
    @JsonField
    private SalutDevice device;
    @JsonField
    private boolean isHost = false;

    public Player() {}

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
    }

    public void removeCards() {
        cards.clear();
    }

    public int getCurrentBid() { return currentBid; }

    public void setCurrentBid(int bid) { currentBid = bid; }

    public int giveBlind(int blind) {
        if (chipCount < blind) {
            currentBid = chipCount;
            setAllIn();
        }
        else {
            currentBid = blind;
        }

        return currentBid; // return amount is either the required blind or the whole chipCount if it's less than the blind
    }

    public boolean hasFolded() {
        return hasFolded;
    }

    public boolean getIsAllIn() {
        return isAllIn;
    }

    public void setIsAllIn(boolean allIn) {
        isAllIn = allIn;
    }

    public boolean isAllIn() {
        return isAllIn;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setIsDealer(boolean isDealer) {
        this.isDealer = isDealer;
    }

    public void payOutPot(int pot) {
        chipCount += pot;
    }

    public void setDeviceId(String id) {
        this.deviceId = id;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void activate() {
        cards.clear();
        hasFolded = false;
        isAllIn = false;
        isSmallBlind = false;
        isBigBlind = false;
        checkStatus = false;
        cheatStatus = false;
        status = "";
    }

    public void setCheckStatus(boolean status) {
        checkStatus = status;
    }

    public boolean getCheckStatus() {
        return checkStatus;
    }


    public void reduceChipCount(int amountOfReduce) {
        chipCount-=amountOfReduce;
    }

    public void raiseChipCount(int amountOfRaise) {
        chipCount+=amountOfRaise;
    }

    public void setCheatStatus(boolean hasCheated) {
        this.cheatStatus=hasCheated;
    }

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
        cards.clear();
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
        parcel.writeByte((byte) (this.isHost ? 1 : 0));
        parcel.writeString(this.deviceId);
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
        this.checkStatus = in.readByte() != 0;
        this.status = in.readString();
        this.isHost = in.readByte() != 0;
        this.deviceId = in.readString();
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
        this.deviceId = device.deviceName;
    }

    public SalutDevice getDevice() {
        return device;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHasFolded(boolean hasFolded) {
        this.hasFolded = hasFolded;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
  
    public Card getCard1() {
        if (cards.size() > 0)
            return cards.get(0);

        return null;
    }

    public Card getCard2() {
        if (cards.size() > 1)
            return cards.get(1);

        return null;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setIsHost(boolean host) {
        isHost = host;
    }

    public void replaceCard(boolean replaceCard1, Card replacementCard) {
        int cardToReplace = 0;

        if (!replaceCard1)
            cardToReplace = 1;

        cards.set(cardToReplace, replacementCard);
    }
}