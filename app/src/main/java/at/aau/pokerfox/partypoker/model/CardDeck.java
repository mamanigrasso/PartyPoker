package at.aau.pokerfox.partypoker.model;


import java.util.ArrayList;
import java.util.Random;

public class CardDeck {

    private static ArrayList<Card> cards;
    private static CardDeck DECK = new CardDeck();


    public static Card issueNextCardFromDeck() {
        return new Card((int)(1+Math.random()*3), (int)(4+Math.random()*10));
    }

    public static void randomizeDeck() {

        Card temp;
        int index_1, index_2;
        Random generator = new Random();

        for(int i = 0;i<100;i++)
        {
            index_1 = generator.nextInt(cards.size() -1);
            index_2 = generator.nextInt(cards.size() -1);

            temp = cards.get(index_2);
            cards.set(index_2,cards.get(index_1));
            cards.set(index_1,temp);
        }
    }

    public static void fillUp() {

        cards = new ArrayList<Card>();
        for(int a=0;a<=3;a++){
            for(int b=0;b<=12;b++){
                cards.add(new Card(a,b));
            }
        }
    }
}
