package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.os.AsyncTask;

public class Player {
    private String name;
    private boolean isAllIn = false;
    private boolean hasFolded = false;
    private boolean isDealer = false;
    private int chipCount;
    private int currentBid;
    private ArrayList<Card> cards = new ArrayList<Card>();;
    private boolean cheatStatus = false;
    private boolean checkStatus = false;

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
            chipCount -= blind;
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
        System.out.println(name + " is ALL-IN!!!");
    }

    public void setFolded() {
        hasFolded = true;
    }
}