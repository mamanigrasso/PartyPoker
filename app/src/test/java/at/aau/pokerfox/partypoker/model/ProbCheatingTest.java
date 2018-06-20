package at.aau.pokerfox.partypoker.model;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Andreas on 13.06.2018.
 */

@RunWith(Parameterized.class)
public class ProbCheatingTest {

    private ProbCheating pc =null;



    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object [] [] {
                {0, 0, 2, 9, 2, 4, 2, 6, 2, 2}, {0, 0, 0, 1, 3, 2, 3, 4, 3, 5},
                {0, 3, 2, 3, 0, 3, 2, 5, 3, 4}, {0, 8, 0, 9, 0, 10, 2, 5, 2, 4},
                {2, 2, 0, 2, 0, 7, 2, 7, 2, 4}, {0, 0, 0, 9, 0, 10, 0, 11, 0, 12},
                {0, 0, 0, 1, 0, 2, 0, 4, 0, 5}, {0, 0, 0, 1, 3, 2, 3, 2, 3, 3},
                {0, 0, 0, 1, 3, 1, 3, 9, 3, 6}
        });
    }

    @Parameterized.Parameter(0)
    public int suit1;

    @Parameterized.Parameter(1)
    public int rank1;

    @Parameterized.Parameter(2)
    public int suit2;

    @Parameterized.Parameter(3)
    public int rank2;

    @Parameterized.Parameter(4)
    public int suit3;

    @Parameterized.Parameter(5)
    public int rank3;

    @Parameterized.Parameter(6)
    public int suit4;

    @Parameterized.Parameter(7)
    public int rank4;

    @Parameterized.Parameter(8)
    public int suit5;

    @Parameterized.Parameter(9)
    public int rank5;


    @Before
    public void setUp() throws Exception {
        pc = new ProbCheating();

    }

    @After
    public void tearDown() throws Exception {
        pc = null;
    }

   public void initAddSortCards() {

        Card card1 = new Card(suit1,rank1);
        Card card2 = new Card(suit2,rank2);
        Card card3 = new Card(suit3,rank3);
        Card card4 = new Card(suit4,rank4);
        Card card5 = new Card(suit5,rank5);

       pc.addCardsToArrayList(card1);
       pc.addCardsToArrayList(card2);
       pc.addCardsToArrayList(card3);
       pc.addCardsToArrayList(card4);
       pc.addCardsToArrayList(card5);

       pc.sortAllCardsOfCheater();
    }


   @Test
    public void probThreeOfAKindDraw() {

        initAddSortCards();
       System.out.println(pc.probThreeOfAKindDraw(pc.counterOfSameRank()));
    }

   @Test
    public void probFourOfAKindDraw() throws Exception {
        initAddSortCards();
       System.out.println(pc.probFourOfAKindDraw(pc.counterOfSameRank()));

    }

    @Test
    public void probFullHouseDraw() throws Exception {
        initAddSortCards();
        System.out.println(pc.probFullHouseDraw(pc.counterOfSameRank()));

    }

    @Test
    public void probFlushDraw() throws Exception {
        initAddSortCards();
        System.out.println(pc.probFlushDraw(pc.countUsesSameSuit()));

    }

    @Test
    public void probStraightFlushDraw() throws Exception {
        initAddSortCards();
        System.out.println(pc.probStraightFlushDraw());

    }

    @Test
    public void probRoyalFlushDraw() throws Exception {
        initAddSortCards();
        System.out.println(pc.probRoyalFlushDraw());

    }

    @Test
    public void sortAllCardsOfCheater() throws Exception {
        initAddSortCards();
        pc.sortAllCardsOfCheater();
    }

    @Test
    public void probStreetDraw() throws Exception {
        initAddSortCards();
        System.out.println(pc.probStreetDraw());

    }

    @Test
    public void probCheat() throws Exception {
        ArrayList<Card> testList = new ArrayList<>();
        Card card1 = new Card(suit1,rank1);
        Card card2 = new Card(suit2,rank2);
        Card card3 = new Card(suit3,rank3);
        Card card4 = new Card(suit4,rank4);
        Card card5 = new Card(suit5,rank5);
        testList.add(card1);
        testList.add(card2);
        testList.add(card3);
        testList.add(card4);
        testList.add(card5);
        pc.sortAllCardsOfCheater();

        System.out.println(pc.probCheat(testList));
    }

}