package at.aau.pokerfox.partypoker.model;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by TimoS on 22.06.2018.
 */

public class ModelTests {

    Player testPlayer = null;
    Player testPlayer2 = null;

    @Before
    public void onStartup() {
        testPlayer = new Player();
        testPlayer.setName("harald");
        testPlayer2 = new Player("heinz");
    }

    @Test
    public void testPlayerNames() {
        assertTrue(testPlayer.getName() == "harald");
        assertTrue(testPlayer2.getName() == "heinz");

    }

    @Test
    public void testActivate() {
        Card card2 = new Card(0,2);
        Card card3 = new Card(1,3);
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(card2);
        cards.add(card3);

        testPlayer.activate();
        testPlayer.setCheatStatus(true);
        testPlayer.setChipCount(50);
        testPlayer.setCards(cards);
        testPlayer.setCurrentBid(50);
        testPlayer.setCheckStatus(true);
        testPlayer.setDeviceId("blubb");
        testPlayer.setIsDealer(false);
        testPlayer.setFolded();

        assertEquals(cards, testPlayer.getCards());
        assertTrue(testPlayer.getCheatStatus());
        assertTrue(testPlayer.getChipCount() == 0);
        assertTrue(testPlayer.getCurrentBid() == 50);
        assertTrue(testPlayer.getCheckStatus());
        assertTrue(testPlayer.getDeviceId() == "blubb");
        assertTrue(testPlayer.isDealer());
        assertTrue(testPlayer.hasFolded());

    }

    @Test
    public void winnerTaskTest() {
        ShowWinnerTask showWinnerTask = new ShowWinnerTask();
        showWinnerTask.execute();
        assertTrue(true);
    }
}
