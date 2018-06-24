package at.aau.pokerfox.partypoker;

import android.app.Activity;
import android.content.Context;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.aau.pokerfox.partypoker.model.network.MessageHandler;

public class PPATest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testIsHostTrue() {
        PartyPokerApplication.setIsHost(true);
        Assert.assertEquals(true, PartyPokerApplication.isHost());
    }

    @Test
    public void testIsHostFalse() {
        PartyPokerApplication.setIsHost(false);
        Assert.assertEquals(false, PartyPokerApplication.isHost());
    }

    @Test
    public void testMessageHandlerSingleton() {
        MessageHandler messageHandler = PartyPokerApplication.getMessageHandler();
        Assert.assertEquals(messageHandler, PartyPokerApplication.getMessageHandler());
    }
}
