package at.aau.pokerfox.partypoker.model;



public class CardDeck {

    private static CardDeck DECK = new CardDeck();

    public enum Suits{
        CLUBS,HEARTS,DIAMONDS,SPADES
    }

    public enum Numerals{
        DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        TEN, JACK(), QUEEN, KINGG, ACE;
    }

    public static Card issueNextCardFromDeck() {
        return new Card((int)(1+Math.random()*3), (int)(4+Math.random()*10));
    }

    public static void randomizeDeck() {

    }

    public static void fillUp() {

    }
}
