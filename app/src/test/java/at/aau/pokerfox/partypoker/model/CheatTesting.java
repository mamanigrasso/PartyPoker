package at.aau.pokerfox.partypoker.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Andreas on 15.05.2018.
 */
public class CheatTesting {

    Cheat letsCheat;


    private ArrayList<Player> allPlayers;

    private ArrayList<Player> cheaters;
    private ArrayList<Player> honests;

    private int chipCounts = 1000;

    Player andy = new Player("Andy");
    Player michael = new Player ("Michael");
    Player mathias = new Player ("Mathias");

    Player manuel = new Player("Manuel");
    Player marco = new Player ("Marco");
    Player timo = new Player ("Timo");


    @Before
    public void setUp() {
        //This are the Cheaters
        andy.setCheatStaus(true);
        michael.setCheatStaus(true);
        mathias.setCheatStaus(true);


        cheaters = new ArrayList<>();
        honests = new ArrayList<>();
        allPlayers = new ArrayList<>();
        letsCheat = new Cheat();

        cheaters.add(andy);
        cheaters.add(michael);
        cheaters.add(mathias);

        honests.add(manuel);
        honests.add(marco);
        honests.add(timo);

        allPlayers.addAll(cheaters);
        allPlayers.addAll(honests);

        setChipCounts(chipCounts);
    }

    @After
    public void tearDown() {
        cheaters = null;   //cheaters.clear() would also work, if we would initialise the List outside the setUp()-Method.
        honests = null;

        allPlayers = null;
        letsCheat = null;
    }

    @Test
    public void ditHeCheatTestWasCheatingTrue() {

        int penalty = chipCounts/5;
        letsCheat.ditHeCheat(allPlayers, timo, andy, penalty);

        assertEquals((long)chipCounts-penalty,(long)andy.getChipCount());
        assertEquals((long)chipCounts+penalty,(long)timo.getChipCount());

    }

    @Test
    public void ditHeCheatTestWasCheatingFalse() {

        int penalty = chipCounts/5;

        letsCheat.ditHeCheat(allPlayers, andy, timo, penalty);

        assertEquals((long)chipCounts-penalty,(long)andy.getChipCount());
        assertEquals((long)chipCounts+penalty,(long)timo.getChipCount());

    }

    @Test
    public void ditHeCheatTestClickOnYourself() {

        int penalty = chipCounts/5;

        letsCheat.ditHeCheat(allPlayers, andy, andy, penalty);
        //Nothing happens because on the one hand you get Chips and on the other hand you lose chips.
        assertEquals((long)chipCounts,(long)andy.getChipCount());

    }


    private void setChipCounts (int chips) {
        for(int i = 0 ; i <allPlayers.size(); i++) {
            allPlayers.get(i).setChipCount(chips);
        }
    }
}