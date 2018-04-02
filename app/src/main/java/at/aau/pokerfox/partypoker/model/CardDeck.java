package at.aau.pokerfox.partypoker.model;

import at.aau.pokerfox.partypoker.Card;

public class CardDeck {
    private static CardDeck DECK = new CardDeck();

    public static Card issueNextCardFromDeck() {
        return new Card((int)(1+Math.random()*3), (int)(4+Math.random()*10));
    }

    public static void randomizeDeck() {

    }

    public static void fillUp() {

    }
}
