package at.aau.pokerfox.partypoker.model;

import android.support.annotation.NonNull;

/**
 * Created by Marco on 4/10/2018.
 *
 * Source from CrazyJugglerDrummer
 * <p/>
 *
 * <p/>https://www.codeproject.com/Articles/38821/Make-a-poker-hand-evalutator-in-Java
 * Adapted by mamanigrasso to evaluate 7-card-hands instead of 5-card-hands.
 */

public class Hand {
    private Card[] cards;
    private int[] value;

    Hand(@NonNull Card[] passedCards)
    {
        // cards must always be 7 (2 in hand and 5 on the table)
        if (passedCards.length != 7) {
            throw new IllegalArgumentException("Cards in a hand must always be 7. 2 in hand and 5" +
                    " on the table!");
        }

        this.cards = passedCards;
        this.value = new int[6];

        int[] ranks = new int[14];
        int[] orderedRanks = new int[7];
        boolean flush=true, straight=false;
        int sameCards=1,sameCards2=1;
        int largeGroupRank=0,smallGroupRank=0;
        int index=0;
        int topStraightValue=0;

        // init the rank array
        for (int x = 0; x < 14; x++) {
            ranks[x] = 0;
        }

        // fill the ranks array
        for (int x = 0; x < 7; x++) {
            ranks[cards[x].getRank()]++;
        }

        // check for flush
        int cntHearts = 0, cntSpades = 0, cntDiamonds = 0, cntClubs = 0;
        for (int x = 0; x < 7; x++) {
            if (cards[x].getSuit() == 0)
                cntHearts++;
            if (cards[x].getSuit() == 1)
                cntSpades++;
            if (cards[x].getSuit() == 2)
                cntDiamonds++;
            if (cards[x].getSuit() == 3)
                cntClubs++;
        }

        if (cntHearts < 5 && cntSpades < 5 && cntDiamonds < 5 && cntClubs < 5)
            flush = false;

        for (int x = 13; x >= 0; x--) {

            if (ranks[x] > sameCards) {

                if (sameCards != 1) {
                //if sameCards was not the default value
                    sameCards2 = sameCards;
                    smallGroupRank = largeGroupRank;
                }

                sameCards = ranks[x];
                largeGroupRank = x;

            } else if (ranks[x] > sameCards2) {
                sameCards2 = ranks[x];
                smallGroupRank = x;
            }
        }

        // check high card
        if (ranks[1]==1) { //if ace, run this before because ace is highest card
            orderedRanks[index] = 14;
            index++;
        }

        for (int x = 13; x > 1; x--) {
            if (ranks[x] == 1) {
                orderedRanks[index] = x; //if ace
                index++;
            }
        }

        // check for straight
        for (int x = 1; x < 10; x++) { //can't have straight with lowest value of more than 10
            if (ranks[x] == 1 && ranks[x+1] == 1 && ranks[x+2] == 1 && ranks[x+3] == 1 && ranks[x+4] == 1) {
                straight = true;
                topStraightValue = x+4; //4 above bottom value
                break;
            }
        }

        // if the straight is 10, Jack, Qeen, King, Ace
        if (ranks[10] == 1 && ranks[11] == 1 && ranks[12] == 1 && ranks[13] == 1 && ranks[1] == 1) { //ace high
            straight = true;
            topStraightValue = 14; //higher than king
        }

        // init value array
        for (int x=0; x < 6; x++) {
            value[x] = 0;
        }


        // start hand evaluation
        // high card
        if (sameCards == 1) {
            value[0] = 1;
            value[1] = orderedRanks[0];
            value[2] = orderedRanks[1];
            value[3] = orderedRanks[2];
            value[4] = orderedRanks[3];
            value[5] = orderedRanks[4];
        }

        // one pair
        if (sameCards == 2 && sameCards2 == 1) {
            value[0] = 2;
            value[1] = largeGroupRank; //rank of pair
            value[2] = orderedRanks[0];
            value[3] = orderedRanks[1];
            value[4] = orderedRanks[2];
        }

        // two pair
        if (sameCards == 2 && sameCards2 == 2) {
            value[0] = 3;
            //rank of greater pair
            value[1] = largeGroupRank > smallGroupRank ? largeGroupRank : smallGroupRank;
            value[2] = largeGroupRank < smallGroupRank ? largeGroupRank : smallGroupRank;
            value[3] = orderedRanks[0];  //extra card
        }

        // three of a kind
        if (sameCards == 3 && sameCards2 != 2) {
            value[0] = 4;
            value[1] = largeGroupRank;
            value[2] = orderedRanks[0];
            value[3] = orderedRanks[1];
        }

        // straight
        if (straight && !flush) {
            value[0] = 5;
            value[1] = topStraightValue;
        }

        // flush
        if (flush && !straight) {
            value[0] = 6;
            value[1] = orderedRanks[0]; //tie determined by ranks of cards
            value[2] = orderedRanks[1];
            value[3] = orderedRanks[2];
            value[4] = orderedRanks[3];
            value[5] = orderedRanks[4];
        }

        // full house
        if (sameCards == 3 && sameCards2 == 2) {
            value[0] = 7;
            value[1] = largeGroupRank;
            value[2] = smallGroupRank;
        }

        // poker
        if (sameCards == 4) {
            value[0] = 8;
            value[1] = largeGroupRank;
            value[2] = orderedRanks[0];
        }

        // straight flush
        if (straight && flush) {
            value[0] = 9;
            value[1] = topStraightValue;
        }
    }

    public String display()
    {
        String s;
        switch( value[0] )
        {

            case 1:
                s="high card";
                break;
            case 2:
                s="pair of " + Card.rankAsString(value[1]) + "\'s";
                break;
            case 3:
                s="two pair " + Card.rankAsString(value[1]) + " " +
                        Card.rankAsString(value[2]);
                break;
            case 4:
                s="three of a kind " + Card.rankAsString(value[1]) + "\'s";
                break;
            case 5:
                s=Card.rankAsString(value[1]) + " high straight";
                break;
            case 6:
                s="flush";
                break;
            case 7:
                s="full house " + Card.rankAsString(value[1]) + " over " +
                        Card.rankAsString(value[2]);
                break;
            case 8:
                s="four of a kind " + Card.rankAsString(value[1]);
                break;
            case 9:
                s="straight flush " + Card.rankAsString(value[1]) + " high";
                break;
            default:
                s="error in Hand.display: value[0] contains invalid value";
        }

        return s;
    }

    void displayAll()
    {
        for (int x=0; x<5; x++)
            System.out.println(cards[x]);
    }

    int compareTo(Hand that)
    {
        for (int x=0; x<6; x++)
        {
            if (this.value[x]>that.value[x])
                return 1;
            else if (this.value[x]<that.value[x])
                return -1;
        }
        return 0; //if hands are equal
    }
}
