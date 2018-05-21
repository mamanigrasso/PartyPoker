package at.aau.pokerfox.partypoker.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

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

    private static ModActInterface modActInterface;


    Player andy = new Player("Andy");
    Player michael = new Player ("Michael");
    Player mathias = new Player ("Mathias");

    Player manuel = new Player("Manuel");
    Player marco = new Player ("Marco");
    Player timo = new Player ("Timo");
    Player player1 = new Player ("Player 1");


    @Before
    public void setUp() throws Exception {
        Game.init(50,100,1000,6,modActInterface);
    }

    @After
    public void tearDown() throws Exception {
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
        Game.addPlayer(andy);
        Game.addPlayer(michael);

        Game.getInstance().startGame();
        Game.getInstance().nextStep();
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

    @Test
    public void getInstance() throws Exception {
        Game.getInstance();
    }

    @Test
    public void addPlayer() throws Exception {
        assertTrue(Game.addPlayer(andy));
    }

    @Test
    public void addToMuchPlayers() throws Exception {
        assertTrue(Game.addPlayer(andy));
        assertTrue(Game.addPlayer(michael));
        assertTrue(Game.addPlayer(mathias));
        assertTrue(Game.addPlayer(timo));
        assertTrue(Game.addPlayer(marco));
        assertTrue(Game.addPlayer(manuel));
        assertFalse(Game.addPlayer(marco)); //maxSize = 6
    }

    @Test
    public void removePlayer() throws Exception {
        assertTrue(Game.addPlayer(andy));
        assertTrue(Game.addPlayer(michael));
        assertTrue(Game.addPlayer(mathias));
        assertTrue(Game.addPlayer(timo));
        assertTrue(Game.addPlayer(marco));
        assertTrue(Game.addPlayer(manuel));

        assertTrue(Game.removePlayer(manuel));
        assertTrue(Game.removePlayer(marco));
        assertTrue(Game.removePlayer(timo));
        assertTrue(Game.removePlayer(mathias));
        assertTrue(Game.removePlayer(michael));  //there is one left

    }
    @Test
    public void removeAllPlayer() throws Exception {
        assertTrue(Game.addPlayer(andy));
        assertTrue(Game.addPlayer(michael));
        assertTrue(Game.addPlayer(mathias));
        assertTrue(Game.addPlayer(timo));
        assertTrue(Game.addPlayer(marco));
        assertTrue(Game.addPlayer(manuel));

        assertTrue(Game.removePlayer(manuel));
        assertTrue(Game.removePlayer(marco));
        assertTrue(Game.removePlayer(timo));
        assertTrue(Game.removePlayer(mathias));
        assertTrue(Game.removePlayer(michael));
        assertFalse(Game.removePlayer(andy));   //last one can´t be removed so assertFalse
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

}