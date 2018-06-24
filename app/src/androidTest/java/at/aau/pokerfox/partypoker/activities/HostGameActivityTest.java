package at.aau.pokerfox.partypoker.activities;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import at.aau.pokerfox.partypoker.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Andreas on 24.06.2018.
 */
public class HostGameActivityTest {

    @Rule
    public ActivityTestRule<HostGameActivity> hostGameTestRule = new ActivityTestRule<HostGameActivity>(HostGameActivity.class);
    private HostGameActivity hostGame = null;


    @Before
    public void setUp() throws Exception {
        hostGame = hostGameTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        hostGame=null;
    }



}