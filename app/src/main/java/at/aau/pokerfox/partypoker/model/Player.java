package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;

public class Player {
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

    public int getAndResetCurrentBid() {
        int returnAmount = currentBid;
        currentBid = 0;
        return returnAmount;
    }

    public int giveBlind(int blind) {
        int returnAmount = blind;

        if (chipCount < blind) {
            returnAmount = chipCount;
            currentBid = chipCount;
            chipCount = 0;
            isAllIn = true;
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
                    if (chipCount > 0) {
                        returnAmount = chipCount + currentBid;
                        System.out.println(name + " bet: " + returnAmount);
                    } else
                        returnAmount = 0;
                }
            } else {
                if (chipCount > returnAmount * 2) {
                    returnAmount = amount * 2;
                    System.out.println(name + " raised: " + returnAmount);
                } else {
                    if (chipCount > 0) {
                        returnAmount = chipCount + currentBid;
                        chipCount = 0;
                        System.out.println(name + " raised: " + returnAmount);
                    } else
                        returnAmount = 0;
                }
            }
        }

        else if (Math.random()*2 > Math.random()) {
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
                    chipCount = 0;
                    isAllIn = true;
                }
                System.out.println(name + " called: " + returnAmount);

            }
        }

        if (chipCount == 0) {
            System.out.println(name + " is ALL-IN!!!");
            isAllIn = true;
        } else {
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
}