package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;

/**
 * Created by Andreas on 13.06.2018.
 */

public class ProbCheating {

    private ArrayList<Card> allCardsOfCheater = new ArrayList<>();

    private ArrayList<Card> streetCards = new ArrayList<>(5);
    boolean aceIsLastCardInStreet = false;

    //How often do we have the same Ranks -> saved in an integer-Array
    //Important for three-, fourOfAKind, FullHouse
    public int[] counterOfSameRank() {

        int[] timesOfSameRanks = new int[2];

        boolean rankCounterIsBeingRaised = true;
        int sameRankACounter = 0;
        int sameRankBCounter = 0;
        int countTester = 0;

        while (rankCounterIsBeingRaised == true) {
            for (int i = 1; i < allCardsOfCheater.size(); i++) {

                //in this case there we have same ranks up from the beginning -> A-Counter raises
                if (sameRankBCounter == 0 && allCardsOfCheater.get(i).getRank() == allCardsOfCheater.get(i - 1).getRank()) {
                    sameRankACounter++;
                }
                //in this case there is a gap between two ranks -> A-Counter didn´t raise
                if (sameRankACounter == countTester) { //RankCounter wasn´t raised in this round
                    rankCounterIsBeingRaised = false;
                    //if we have the gab, but the next 2 Ranks would be the same -> B-Counter raises --> important for FullHouseDraw
                    if (i <= 3 && allCardsOfCheater.get(i + 1).getRank() == allCardsOfCheater.get(i).getRank() && sameRankACounter > 0) {
                        sameRankBCounter++;
                    }
                }

                countTester = sameRankACounter;
            }
        }

        timesOfSameRanks[0] = sameRankACounter;  //First Number in the Array: How often A-Rank in the Playercards+Flop
        timesOfSameRanks[1] = sameRankBCounter;  //Second Number in the Array: How often B-Rank in the Playercards+Flop

        return timesOfSameRanks;
    }

    public int probThreeOfAKindDraw(int[] sameRanks) {
        int probThreeOfAKind = 0;

        if (sameRanks[0] == 1 && sameRanks[1] == 0) {
            probThreeOfAKind = 2 * 4;  //2 Outs if we have one pair
        } else if (sameRanks[0] == 0 && sameRanks[1] == 1) {
            probThreeOfAKind = 2 * 4;  //2 Outs if we have one pair
        } else if (sameRanks[0]==2 || sameRanks[1]==2) {
            probThreeOfAKind = 100;  //on threeOfAKind
        }
        return probThreeOfAKind;
    }

    public int probFourOfAKindDraw(int[] sameRanks) {
        int probFourOfAKind = 0;

        if ((sameRanks[0] == 2 && sameRanks[1] <= 1)||(sameRanks [0] <= 1 && sameRanks[1] == 2)) {
            probFourOfAKind = 1 * 4;  //1 Out ´cause we have a threeOfAKind
        } else if (sameRanks[0] == 3 || sameRanks[1] == 3) {
            probFourOfAKind = 100;    // We have a fourOfAKind
        }
        return probFourOfAKind;
    }

    public int probFullHouseDraw(int[] sameRanks) {
        int probFullHouse = 0;

        if (sameRanks[0] == 1 && sameRanks[1] == 1) {
            probFullHouse = 4 * 4; //4 Outs, ´cause there are 2 pairs with 2 possible cards for a full-house each.
        } else if ((sameRanks[0] == 2 && sameRanks[1] == 1) || (sameRanks[0] == 1 && sameRanks[1] == 2)) {
            probFullHouse = 100;
        }
        return probFullHouse;
    }
    //How often are there same suits --> important for "probFlushDraw" e.g.
    public int countUsesSameSuit() {
        int inter = 0;
        int counterUsesSameSuit = 0;  //How often is the same Suit used eg. 2-2-2-3-1 --> 2 ist 3 times used -> int = 3

        for (int z = 0; z < allCardsOfCheater.size(); z++) {
            for (int i = 0; i < allCardsOfCheater.size(); i++) {
                if (allCardsOfCheater.get(i).getSuit() == z) {
                    inter++;
                }
            }
            if (counterUsesSameSuit < inter) {
                counterUsesSameSuit = inter;
            }
            inter = 0;
        }

        return counterUsesSameSuit;
    }

    public int probFlushDraw(int counterSameSuitMax) {
        int probFlush = 0;
        if (counterSameSuitMax == 4) {
            probFlush = 9 * 4; //9 Outs -> 4 uncovered = 9 left with same suits
        } else if (counterSameSuitMax == 5){
            probFlush = 100;
        }
        return probFlush;
    }

    //Takes the result of the "probStreetDraw()" and then checks if there are same suits too
    public int probStraightFlushDraw() {
        int probStraightFlush = 0;

        int z = probStreetDraw();
        char streetResult;

        switch (z) {
            case 100:
                streetResult = 'A';  //100% Street
                break;
            case 32:
                streetResult = 'B';  //4 Following Ranks
                break;
            case 16:
                streetResult = 'C'; //AceAtLast OR AceAtBeginning OR 2*2 Following Ranks with one distance 2 between OR doubled Ranks
                break;
            case 0:
                streetResult = 'D'; //0% Street
                break;
            default:
                streetResult = 'Z'; //Default
        }

        if(streetResult=='A') {
            if(testForSameSuit()==true) {
                probStraightFlush = 100; // 100% StraightFlush ´cause 5 Cards in the same rank and suit
            }
        }

        if(streetResult=='B') {
            if(testForSameSuit()==true) {
                probStraightFlush = 2*4; //2 Outs - at the beginning and at the end
            }
        }
        //if aceIsLastCardInStreet would be the we would count the probability of "RoyalFlushDraw"
        if (streetResult=='C'&&aceIsLastCardInStreet == false) {

            int counter = 0;
            boolean sameSuit = false;
            boolean doubled = false;
            boolean sameRank = false;

            for (int i = 1; i < streetCards.size(); i++) {
                if (streetCards.get(i).getRank() == streetCards.get(i - 1).getRank() && doubled == false) {
                    doubled = true;
                }
                if (streetCards.get(i).getSuit() == streetCards.get(i - 1).getSuit()) {
                    sameSuit = true;
                } else if (streetCards.get(i).getSuit() != streetCards.get(i - 1).getSuit() && doubled == true) {
                    if (streetCards.get(i).getSuit() == streetCards.get(i - 2).getSuit()) {
                        sameSuit = true;
                    } else {
                        sameSuit = false;
                        break;
                    }
                }
            }

            if (sameSuit == true) {
                probStraightFlush = 1 * 4; //1 Out if 1. aceIsAtBeginning; 2. one rank is doubled; 3. one distance 2
                //System.out.println("Fat if-else works");
            }
        }

        if(streetResult=='D') {
            probStraightFlush = 0;
        }

        return probStraightFlush;
    }

    //Highest possible hand
    public int probRoyalFlushDraw () {
        int probRoyalFlush = 0;

        aceAtLast(); //call method to check if there is the ace at last in the royalFlush
        if(aceIsLastCardInStreet==true) {
            if(testForSameSuit()==true  && streetCards.size()==4) {
                probRoyalFlush = 1*4; //1 Out if there is a Draw
            } else if (testForSameSuit()==true && streetCards.size()==5) {
                probRoyalFlush = 100; //RoyalFlush on 2 hand- and 3 tablecards
            }
        }

        return probRoyalFlush;
    }

    //Test if the cards have the same suits
    public boolean testForSameSuit () {
        boolean sameSuit = false;

        for (int i = 1; i < streetCards.size(); i++) {
            if (streetCards.get(i).getSuit() == streetCards.get(i - 1).getSuit()) {
                sameSuit=true;
            } else {
                sameSuit=false;
                break;
            }
        }

        return sameSuit;
    }


      /*  public int testAufDrillingVierling () {

            int currentSuit = 0;


            int z = 0;


            for (int y = 0; y < 4; y++) {

                if (z > maxUseSameSuit) {
                    maxUseSameSuit = z;
                }
                z = 0;

                for (int i = 0; i < 5; i++) {
                    currentSuit = i;
                    if (allCardsOfCheater.get(i).getSuit() == currentSuit) {
                        z++;
                    }
                }
                if (z == maxUseSameSuit && howOftensameMaxSuite < 1) {
                    howOftensameMaxSuite++;

                }

            }


            System.out.println("maxusesamesuit" + maxUseSameSuit);
            System.out.println("howoftensamemaxsuite" + howOftensameMaxSuite);
            return maxUseSameSuit;
        }

        public void wahrDrillingVierlingFullhouse () {

            int wahrDrilling = 0;
            int wahrVierling = 0;
            int wahrFullHouse = 0;


            int highestWahr = 0;


            if (testAufDrillingVierling() == 2 && howOftensameMaxSuite == 1) {
                wahrFullHouse = 4 * 4;
            }
            if (testAufDrillingVierling() == 3) {
                wahrVierling = 1 * 4;
            }
            if (testAufDrillingVierling() == 2 && howOftensameMaxSuite == 0) {
                wahrDrilling = 2 * 4;
            }

            if (wahrFullHouse != 0) {
                highestWahr = wahrFullHouse;
                System.out.println("Full-House: ");
            } else if (wahrVierling != 0) {
                highestWahr = wahrVierling;
                System.out.println("Vierling: ");
            } else if (wahrDrilling != 0) {
                highestWahr = wahrDrilling;
                System.out.println("Drilling: ");
            }

            System.out.println(highestWahr);
        }*/

    //Sorts the cards according to their ranks -> bottom-up
    public void sortAllCardsOfCheater () {

        Card intermediate;
        for (int z = 0; z < allCardsOfCheater.size() - 1; z++) {

            for (int i = 1; i < allCardsOfCheater.size(); i++) {
                if (allCardsOfCheater.get(i).getRank() < allCardsOfCheater.get(i - 1).getRank()) {

                    intermediate = allCardsOfCheater.get(i - 1);
                    allCardsOfCheater.set(i - 1, allCardsOfCheater.get(i));
                    allCardsOfCheater.set(i, intermediate);
                }
            }
        }
        // for (int i = 0; i < allCardsOfCheater.size(); i++) {
        //     System.out.println(allCardsOfCheater.get(i).getRank());
        // }
    }

    public int probStreetDraw () {

        int probForStreet = 0;

        int fixTogether = 0; //the cards should fix together 3 times - then there are 4 following cards
        int doubledRanks = 0; //1 time ok
        int streetBreaker = 0;  //how often is the rank-distance more than 1 - max one time für streetDraw
        int roundCounter = 1;
        boolean aceAtFirst = false;
        boolean streetDrawPossible = false;
        boolean addToStreetFlag = false;

        //Test if ace is last in Street or not
        aceAtLast();
        if(aceIsLastCardInStreet==false) {

            //If ace is not last we can find out if we have a working combination for a street
            for (int i = 1; i < allCardsOfCheater.size(); i++) {
                if (allCardsOfCheater.get(i).getRank() - allCardsOfCheater.get(i - 1).getRank() == 1 && allCardsOfCheater.get(0).getRank() != 0) {
                    streetDrawPossible = true;
                    fixTogether++;

                    if (i == 1) {
                        streetCards.add(allCardsOfCheater.get(0));
                    } else if (i == 2 && addToStreetFlag == false) {
                        streetCards.add(allCardsOfCheater.get(1));
                    }
                    streetCards.add(allCardsOfCheater.get(i));
                    addToStreetFlag = true;

                    //Ace is at the beginning (ace has the Rank 0)
                } else if (allCardsOfCheater.get(i).getRank() - allCardsOfCheater.get(i - 1).getRank() == 1 && allCardsOfCheater.get(0).getRank() == 0) {
                    streetDrawPossible = true;
                    fixTogether++;
                    aceAtFirst = true;
                    if (i == 1) {
                        streetCards.add(allCardsOfCheater.get(0));
                    }
                    streetCards.add(allCardsOfCheater.get(i));
                    addToStreetFlag = true;

                } else if (allCardsOfCheater.get(i).getRank() - allCardsOfCheater.get(i - 1).getRank() == 2 && streetBreaker == 0) {
                    streetDrawPossible = true;
                    streetBreaker++;
                    fixTogether++;
                    if (i == 1) {
                        streetCards.add(allCardsOfCheater.get(0));
                    }
                    streetCards.add(allCardsOfCheater.get(i));
                    addToStreetFlag = true;

                } else if (allCardsOfCheater.get(i).getRank() - allCardsOfCheater.get(i - 1).getRank() == 0 && doubledRanks == 0) {
                    streetDrawPossible = true;
                    doubledRanks++;
                    fixTogether++;
                    if (i == 1) {
                        streetCards.add(allCardsOfCheater.get(0));
                    }
                    streetCards.add(allCardsOfCheater.get(i));
                    addToStreetFlag = true;

                } else if (i == 1) {  //First Round negativ -> StreetDraw is still possible
                    streetDrawPossible = true;

                } else {
                    streetDrawPossible = false;

                    //just in case the first 4 cards lead to a streetDraw
                    if (fixTogether == 3 && streetDrawPossible == false) {
                        streetDrawPossible = true;
                        break;
                    }

                    //All other cases --> no StreetDrawPossible
                    break;
                }

                roundCounter++;
            }
        }


        if (streetDrawPossible == true && fixTogether == 3 && streetBreaker == 0) { //optimal Row of Ranks (distance 1 each between 4 cards)
            probForStreet = 8 * 4;      //8 outs because 4 suits below the lowest card and 4 suits above the highest card
            // System.out.println("optimal Row");
        } else if (streetDrawPossible == true && fixTogether == 3 && aceAtFirst == true) {
            probForStreet = 4 * 4;     //4 outs because ace is at the Beginning of the street
            // System.out.println("Ace at beginning");
        } else if (streetDrawPossible == true && (fixTogether == 3|| fixTogether == 4) && ((streetBreaker == 1 && doubledRanks == 0) ||
                streetBreaker == 0 && doubledRanks ==1 )) {
            probForStreet = 4 * 4;     //4 outs because there has to be this rank in one of the 4 suits in the turn or the river
            // System.out.println("Row with one distance 2 or one doubled rank");
        } else if (aceIsLastCardInStreet == true &&streetCards.size()==4) {
            probForStreet = 4 * 4;     //4 outs because the ace has to be at last
            // System.out.println("Ace at last - Draw");
        } else if ((streetDrawPossible == true && fixTogether == 4 && doubledRanks==0&&streetBreaker==0)||(aceIsLastCardInStreet==true &&streetCards.size()==5)) {
            probForStreet = 100;  //100% Street - with or without aceAtLast
            // System.out.println("Street to 100 %");
        } else if (streetDrawPossible == false) {
            // System.out.println("No streetDraw there");
        }

        return probForStreet;

    }
    //Ace is the special card because, it can be there at the beginning or at the end of the street
    //the code above checks if it is an the beginning now we need to check it at the end of the street
    public void aceAtLast () {


        int counterForAceStreet = 0;  //has to be 3 that a streetDraw is possible
        //First check, if we have a street with ace at last
        if (allCardsOfCheater.get(0).getRank() == 0) { //the ace has the rank 0 so we need to check it
            counterForAceStreet++;
        }
        //Then we take a for-loop to check the higher ranks
        for (int z = 9; z < 13; z++) { //We check from Rank 9 to 12
            for (int i = 1; i < allCardsOfCheater.size(); i++) {
                if (allCardsOfCheater.get(i).getRank() == z) {
                    counterForAceStreet++;
                }
            }
        }

        //Alltogether the counterForAceStreet has to be at least 4 to call it "aceAtLastStreetDraw"
        if (counterForAceStreet >= 4) {
            aceIsLastCardInStreet=true;
        } else {
            aceIsLastCardInStreet=false;
        }

        //If we have this street, we can add the Cheater-Cards to the streetCards
        if (aceIsLastCardInStreet==true) {
            if (allCardsOfCheater.get(0).getRank() == 0) {
                streetCards.add(allCardsOfCheater.get(0));
            }
            for (int z = 9; z < 13; z++) {
                for (int i = 1; i < allCardsOfCheater.size(); i++) {
                    if (allCardsOfCheater.get(i).getRank() == z) {
                        streetCards.add(allCardsOfCheater.get(i));
                    }
                }
            }
        }
    }

    //Method to iterate threw the added street-cards just for fixing bucks
    public void iterateThrewStreetCards () {
        for (int i = 0; i < streetCards.size(); i++) {
            System.out.println(streetCards.get(i).getRank());
        }
    }

    //Shows the prob. from the best combination you can get with one missing card (=DRAW) - appart from FullHouse-Quads (look comment in the method)
    public String probCheat(ArrayList<Card> allCardsOfCheater) {

        this.allCardsOfCheater = allCardsOfCheater;
        String showProb = "";
        sortAllCardsOfCheater();

        if (probRoyalFlushDraw() != 0) {
            streetCards.clear();
            showProb = "Probability for Royal Flush: " + probRoyalFlushDraw() + "%";
        } else if (probStraightFlushDraw() != 0) {
            streetCards.clear();
            showProb = "Probability for Straight Flush: " + probStraightFlushDraw() + "%";
            //If we have a Full House we don´t want to know the prob of a Quads, because we have 100% Full-House
        } else if (probFourOfAKindDraw(counterOfSameRank()) != 0 && probFullHouseDraw(counterOfSameRank())!=100) {
            showProb = "Probability for Quads: " + probFourOfAKindDraw(counterOfSameRank()) + "%";
        } else if (probFullHouseDraw(counterOfSameRank()) != 0) {
            showProb = "Probability for Full House: " + probFullHouseDraw(counterOfSameRank()) + "%";
        } else if (probFlushDraw(countUsesSameSuit()) != 0) {
            streetCards.clear();
            showProb = "Probability for Flush: " + probFlushDraw(countUsesSameSuit()) + "%";
        } else if (probStreetDraw() != 0) {
            streetCards.clear();
            showProb = "Probability for Sraight: " + probStreetDraw() + "%";
        } else if (probThreeOfAKindDraw(counterOfSameRank()) != 0) {
            showProb = "Probability for Trips: " + probThreeOfAKindDraw(counterOfSameRank()) + "%";
        } else {
            showProb = "No mentionable profit probability";
        }

        streetCards.clear();
        return showProb;
    }
}
