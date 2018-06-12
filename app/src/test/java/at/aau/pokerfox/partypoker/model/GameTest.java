package at.aau.pokerfox.partypoker.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.activities.GameActivity;

import static org.junit.Assert.*;

/**
 * Created by Andreas on 16.05.2018.
 */
public class GameTest {

    /*private static final Game _instance = new Game();
    private static LinkedList<Player> allPlayers = new LinkedList<>();
    private static int potSize;
    private static int smallBlind;
    private static int roundsBetweenBlindIncrease;
    private static int startChipCount;
    private static int maxPlayers;
    private static int roundCount = 1;
    private static final int FLOP = 0;
    private static final int TURN = 1;
    private static final int RIVER = 2;
    private static ArrayList<Card> communityCards;
    private static int maxBid = 0;
    private static Player currentPlayer;
    private static int stepID = 1;*/

    private static ModActInterface modActInterface = null;
    private final int SMALL_BLIND = 50;
    private final int CHIP_COUNT = 1000;

    Player andy = new Player("Andy");
    Player michael = new Player ("Michael");
    Player mathias = new Player ("Mathias");

    Player manuel = new Player("Manuel");
    Player marco = new Player ("Marco");
    Player timo = new Player ("Timo");
    Player player1 = new Player ("Player 1");

    Card card1 = new Card(0,1);
    Card card2 = new Card(2,2);
    Card card3 = new Card(1,3);
    Card card4 = new Card(3,4);
    Card card5 = new Card(2,5);
    Card card6 = new Card(1,6);
    Card card7 = new Card(3,7);
    /*Card card8 = new Card(0,0);
    Card card9 = new Card(0,0);
    Card card10 = new Card(1,0);*/

    ArrayList<Player> players;
    ArrayList<Card> cards;

    @Before
    public void setUp() throws Exception {
//        players= new ArrayList<>();
//        cards = new ArrayList<>();
//        PartyPokerApplication.setIsHost(true);
//        boolean isCheatingAllowed = false;
        //Game.getInstance().initGame();
//        Game.init(SMALL_BLIND,100,CHIP_COUNT,6, isCheatingAllowed, modActInterface);
    }

    @After
    public void tearDown() throws Exception {
//        players=null;
//        cards=null;
    }

    @Test
    public void startRound() throws Exception {

    }

    @Test
    public void nextStep() throws Exception {
    }

    @Test
    public void playerDone() throws Exception {
    }

    @Test
    public void playerBid() throws Exception {
    }

    @Test
    public void playerFoldedTest() throws Exception {
    }

    @Test
    public void nextStepTest() throws Exception {  //runs threw the game logic until somebody folded
//        Game.addPlayer(andy);
//        Game.addPlayer(michael);

       // Game.getInstance().startGame();
//        Game.getInstance().nextStep();
        //Game.addPlayer(player1);
        //Game.addPlayer(michael);
        //Game.getInstance().nextStep();
        //Game.getInstance().playerFolded();
    }

    @Test
    public void getMaxBid() throws Exception {
    }

    @Test
    public void startGame() throws Exception {
    }

    @Test
    public void addMyObserver() throws Exception {
    }

    @Test
    public void init() throws Exception {
    }

    @Test (expected =   IllegalStateException.class)
    public void getInstance() throws Exception {
            Game.getInstance();

    }

    @Test
    public void addPlayer() throws Exception {
//        assertTrue(Game.addPlayer(andy));
    }

    @Test
    public void addToMuchPlayers() throws Exception {
//        assertTrue(Game.addPlayer(andy));
//        assertTrue(Game.addPlayer(michael));
//        assertTrue(Game.addPlayer(mathias));
//        assertTrue(Game.addPlayer(timo));
//        assertTrue(Game.addPlayer(marco));
//        assertTrue(Game.addPlayer(manuel));
//        assertFalse(Game.addPlayer(marco)); //maxSize = 6
    }

    @Test
    public void removePlayer() throws Exception {
//        assertTrue(Game.addPlayer(andy));
//        assertTrue(Game.addPlayer(michael));
//        assertTrue(Game.addPlayer(mathias));
//        assertTrue(Game.addPlayer(timo));
//        assertTrue(Game.addPlayer(marco));
//        assertTrue(Game.addPlayer(manuel));
//
//        assertTrue(Game.removePlayer(manuel));
//        assertTrue(Game.removePlayer(marco));
//        assertTrue(Game.removePlayer(timo));
//        assertTrue(Game.removePlayer(mathias));
//        assertTrue(Game.removePlayer(michael));  //there is one left

    }
    @Test
    public void removeAllPlayer() throws Exception {
//        assertTrue(Game.addPlayer(andy));
//        assertTrue(Game.addPlayer(michael));
//        assertTrue(Game.addPlayer(mathias));
//        assertTrue(Game.addPlayer(timo));
//        assertTrue(Game.addPlayer(marco));
//        assertTrue(Game.addPlayer(manuel));
//
//        assertTrue(Game.removePlayer(manuel));
//        assertTrue(Game.removePlayer(marco));
//        assertTrue(Game.removePlayer(timo));
//        assertTrue(Game.removePlayer(mathias));
//        assertTrue(Game.removePlayer(michael));
//        assertFalse(Game.removePlayer(andy));   //last one can´t be removed so assertFalse
    }

    @Test
    public void addObserver() throws Exception {
    }

    @Test
    public void deleteObserver() throws Exception {
    }

    @Test
    public void notifyObservers() throws Exception {
    }

    @Test
    public void notifyObservers1() throws Exception {
    }

    @Test
    public void deleteObservers() throws Exception {
    }

    @Test
    public void setChanged() throws Exception {
    }

    @Test
    public void clearChanged() throws Exception {
    }

    @Test
    public void hasChanged() throws Exception {
    }

    @Test
    public void countObservers() throws Exception {
    }

    @Test
    public void determineWinnerTest() {
//        Card hearts6 = new Card(0,6);
//        Card heartsJack = new Card(0,10);
//        Card heartsAce = new Card(0,0);
//        Card spadesQueen = new Card(1,11);
//        Card diamonds10 = new Card(2,9);
//
//        andy.takeCard(new Card(0,5));
//        andy.takeCard(new Card(0,6));
//        players.add(andy);
//
//        michael.takeCard(new Card(2, 5));
//        michael.takeCard(new Card(2, 6));
//        players.add(michael);
//
//        cards.clear();
//        cards.add(hearts6);
//        cards.add(heartsJack);
//        cards.add(heartsAce);
//        cards.add(spadesQueen);
//        cards.add(diamonds10);
//
//        ArrayList<Player> winners = Game.determineWinner(players,cards);
//
//        assertEquals(1, winners.size()); // one winner
//        assertEquals(andy, winners.get(0));  // andy is winner (hearts flush)
    }

    @Test
    public void areAllPlayersAligned() {
//        players.add(andy);
//        players.add(michael);
//        players.add(marco);
//        Game.addPlayer(andy);
//        Game.addPlayer(michael);
//        Game.addPlayer(marco);
//
//        resetPlayerStates(players);

        // Test1
        // no player has folded, checked or is all in --> not aligned!
//        assertFalse(Game.getInstance().areAllPlayersAligned());
//
//        andy.setHasFolded(true);
//        michael.setCheckStatus(true);
//        marco.setIsAllIn(true);

        // Test2
        // one player has folded, one has checked, one is all in --> aligned!
//        assertTrue(Game.getInstance().areAllPlayersAligned());
//
//        resetPlayerStates(players);
//
//        andy.setHasFolded(true);
//        michael.setCheckStatus(false);
//        marco.setIsAllIn(true);

        // Test3
        // one player has folded, one has not checked yet, one is all in --> aligned!
//        assertFalse(Game.getInstance().areAllPlayersAligned());
    }

    private void resetPlayerStates(ArrayList<Player> players) {
//        for (Player p : players) {
//            p.setIsAllIn(false);
//            p.setHasFolded(false);
//            p.setIsBigBlind(false);
//            p.setIsSmallBlind(false);
//            p.setIsDealer(false);
//            p.setCheckStatus(false);
//        }
    }

    @Test
    public void prepareRound() {
//        players.add(andy);
//        players.add(michael);
//        players.add(marco);
//        Game.addPlayer(andy);
//        Game.addPlayer(michael);
//        Game.addPlayer(marco);
//
//        resetPlayerStates(players);
//
//        andy.setIsDealer(true); // the old dealer
//        marco.setIsBigBlind(true);
//        michael.setIsSmallBlind(true);
//        andy.setIsAllIn(true);
//        michael.setHasFolded(true);
//        marco.setCheckStatus(true);
//
//        Game.getInstance().prepareRound();
//
//        assertFalse(andy.isDealer());
//        assertFalse(michael.isDealer());
//        assertTrue(marco.isDealer());   // must be the new dealer
//
//        for (Player p : players) {
//            assertFalse(p.isSmallBlind());
//            assertFalse(p.isBigBlind());
//            assertFalse(p.hasFolded());
//            assertFalse(p.isAllIn());
//            assertTrue(p.getCard1() == null);
//            assertTrue(p.getCard2() == null);
//        }
    }

    @Test
    public void roundDoneCheckWinner() {
//        Card hearts6 = new Card(0,6);
//        Card heartsJack = new Card(0,10);
//        Card heartsAce = new Card(0,0);
//        Card spadesQueen = new Card(1,11);
//        Card diamonds10 = new Card(2,9);

//        Game.getInstance().addCommunityCard(hearts6);
//        Game.getInstance().addCommunityCard(heartsJack);
//        Game.getInstance().addCommunityCard(heartsAce);
//        Game.getInstance().addCommunityCard(spadesQueen);
//        Game.getInstance().addCommunityCard(diamonds10);

//        andy.takeCard(new Card(0,5));
//        andy.takeCard(new Card(0,6));
//        players.add(andy);
//
//        michael.takeCard(new Card(2, 5));
//        michael.takeCard(new Card(2, 6));
//        players.add(michael);
//
//        Game.addPlayer(andy);
//        Game.addPlayer(michael);
//
//        michael.setIsAllIn(true);
//        assertTrue(Game.getInstance().roundDoneCheckWinner());  // michael is all in and has the worse hand, so we have a final winner
    }

    @Test
    public void assignBlinds() {
//        players.add(andy);
//        players.add(michael);
//
//        Game.addPlayer(andy);
//        Game.addPlayer(michael);
//
//        Game.assignBlinds();
//
//        assertTrue(michael.isSmallBlind());
//        assertFalse(michael.isBigBlind());
//        assertEquals(CHIP_COUNT - SMALL_BLIND, michael.getChipCount());

//        assertFalse(andy.isSmallBlind());
//        assertTrue(andy.isBigBlind());
//        assertEquals(CHIP_COUNT - SMALL_BLIND*2, andy.getChipCount());
    }
}