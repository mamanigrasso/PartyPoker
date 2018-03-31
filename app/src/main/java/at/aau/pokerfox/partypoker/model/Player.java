package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;

public class Player {
    private boolean isDealer = false;
    private boolean isAllIn = false;
    private int chipCount;
    private ArrayList<Card> cards;

    public void setChipCount(int startChips) {
        chipCount = startChips;
    }

    public void takeCard(Card card) {
        cards.add(card);
    }

    public void removeCards() {
        cards.clear();
    }

    public boolean isDealer() {
        return isDealer;
    }

    public int giveBlind(int blind) {
        int returnAmount = blind;

        if (chipCount < blind) {
            returnAmount = chipCount;
            chipCount = 0;
            isAllIn = true;
        }
        else {
            chipCount -= blind;
        }

        return returnAmount; // return amount is either the required blind or the whole chipCount if it's less than the blind
    }

    public int askForAction(int amount) {
        return amount;  // should be the amount specified by the player
    }
}
