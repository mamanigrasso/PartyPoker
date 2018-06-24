package at.aau.pokerfox.partypoker.activities;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.ImageView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import at.aau.pokerfox.partypoker.R;

import static org.junit.Assert.*;

/**
 * Created by Andreas on 24.06.2018.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity= null;

    @Before
    public void setUp() throws Exception {
        mainActivity=mainActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }

    @Test
    public void testLaunch() {
        Button buttonHost = mainActivity.findViewById(R.id.btn_host);

        assertNotNull(buttonHost);
    }
}