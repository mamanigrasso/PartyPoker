package at.aau.pokerfox.partypoker.activities;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ImageView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import at.aau.pokerfox.partypoker.R;
import at.aau.pokerfox.partypoker.model.Game;

import static org.junit.Assert.*;

/**
 * Created by Andreas on 24.06.2018.
 */
public class GameActivityTest {

    @Rule
    public ActivityTestRule<GameActivity> gameActivityActivityTestRule = new ActivityTestRule<GameActivity>(GameActivity.class);

    private GameActivity gameActivity = null;

    @Before
    public void setUp() throws Exception {
        gameActivity = gameActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        gameActivity = null;
    }


}