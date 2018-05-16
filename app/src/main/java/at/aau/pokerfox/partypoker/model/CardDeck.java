package at.aau.pokerfox.partypoker.model;


import java.util.ArrayList;
import java.util.Random;

import at.aau.pokerfox.partypoker.R;

public class CardDeck {

    private static ArrayList<Card> cards;
    private static CardDeck DECK = new CardDeck();
    private static ArrayList<Integer> drawableIdList = new ArrayList<Integer>();
    private static Random random = new Random();


    public static Card issueNextCardFromDeck() {
        return cards.remove(0);
    }

    public static void randomizeDeck() {

        Card temp;
        int index_1, index_2;
        Random generator = new Random();

        for (int i = 0; i < 100; i++) {
            index_1 = generator.nextInt(cards.size() - 1);
            index_2 = generator.nextInt(cards.size() - 1);

            temp = cards.get(index_2);
            cards.set(index_2, cards.get(index_1));
            cards.set(index_1, temp);
        }
    }

    public static void fillUp() {

        cards = new ArrayList<Card>();
        for (int a = 0; a <= 3; a++) {
            for (int b = 0; b <= 12; b++) {
                cards.add(new Card(a, b));
            }
        }
    }

    public static ArrayList<Integer> addDrawableIds() {

        drawableIdList.add(R.drawable.clubs_2);
        drawableIdList.add(R.drawable.clubs_3);
        drawableIdList.add(R.drawable.clubs_4);
        drawableIdList.add(R.drawable.clubs_5);
        drawableIdList.add(R.drawable.clubs_6);
        drawableIdList.add(R.drawable.clubs_7);
        drawableIdList.add(R.drawable.clubs_8);
        drawableIdList.add(R.drawable.clubs_9);
        drawableIdList.add(R.drawable.clubs_10);
        drawableIdList.add(R.drawable.clubs_ace);
        drawableIdList.add(R.drawable.clubs_jack);
        drawableIdList.add(R.drawable.clubs_king);
        drawableIdList.add(R.drawable.clubs_queen);
        drawableIdList.add(R.drawable.spades_2);
        drawableIdList.add(R.drawable.spades_3);
        drawableIdList.add(R.drawable.spades_4);
        drawableIdList.add(R.drawable.spades_5);
        drawableIdList.add(R.drawable.spades_6);
        drawableIdList.add(R.drawable.spades_7);
        drawableIdList.add(R.drawable.spades_8);
        drawableIdList.add(R.drawable.spades_9);
        drawableIdList.add(R.drawable.spades_10);
        drawableIdList.add(R.drawable.spades_ace);
        drawableIdList.add(R.drawable.spades_jack);
        drawableIdList.add(R.drawable.spades_king);
        drawableIdList.add(R.drawable.spades_queen);
        drawableIdList.add(R.drawable.diamonds_2);
        drawableIdList.add(R.drawable.diamonds_3);
        drawableIdList.add(R.drawable.diamonds_4);
        drawableIdList.add(R.drawable.diamonds_5);
        drawableIdList.add(R.drawable.diamonds_6);
        drawableIdList.add(R.drawable.diamonds_7);
        drawableIdList.add(R.drawable.diamonds_8);
        drawableIdList.add(R.drawable.diamonds_9);
        drawableIdList.add(R.drawable.diamonds_10);
        drawableIdList.add(R.drawable.diamonds_ace);
        drawableIdList.add(R.drawable.diamonds_jack);
        drawableIdList.add(R.drawable.diamonds_king);
        drawableIdList.add(R.drawable.diamonds_queen);
        drawableIdList.add(R.drawable.hearts_2);
        drawableIdList.add(R.drawable.hearts_3);
        drawableIdList.add(R.drawable.hearts_4);
        drawableIdList.add(R.drawable.hearts_5);
        drawableIdList.add(R.drawable.hearts_6);
        drawableIdList.add(R.drawable.hearts_7);
        drawableIdList.add(R.drawable.hearts_8);
        drawableIdList.add(R.drawable.hearts_9);
        drawableIdList.add(R.drawable.hearts_10);
        drawableIdList.add(R.drawable.hearts_ace);
        drawableIdList.add(R.drawable.hearts_jack);
        drawableIdList.add(R.drawable.hearts_king);
        drawableIdList.add(R.drawable.hearts_queen);

        return drawableIdList;
    }

    public static int getRandomId(ArrayList<Integer> idList) {
        if (idList.isEmpty()) {
            throw new IllegalArgumentException(
                    "ID List is empty"
            );
        }
        int drawableImageId = idList.get(random.nextInt(idList.size()));

        return drawableImageId;
    }
}
