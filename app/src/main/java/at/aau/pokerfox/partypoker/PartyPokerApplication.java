package at.aau.pokerfox.partypoker;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutDevice;
import com.peak.salut.SalutServiceData;

import java.util.ArrayList;

import at.aau.pokerfox.partypoker.model.network.MessageHandler;
import at.aau.pokerfox.partypoker.model.network.NetworkHelper;

public class PartyPokerApplication extends Application {

    public static final int SALUT_PORT = NetworkHelper.findFreePort();
    public static final String SALUT_SERVICE_NAME = "SAS";

    private static Context appContext;
    private static boolean isHost = false;
    private static MessageHandler messageHandler;
    private static SalutDataReceiver salutDataReceiver;
    private static Salut network;
    private static SalutServiceData salutServiceData;
    private static ArrayList<SalutDevice> connectedDevices = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;

        messageHandler = new MessageHandler();
    }

    public static boolean isHost() {
        return isHost;
    }

    public static void setIsHost(boolean _ishost) {
        isHost = _ishost;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public static SalutDataReceiver getSalutDataReceiver() {
        return salutDataReceiver;
    }

    public static void setSalutDataReceiver(SalutDataReceiver salutDataReceiver) {
        PartyPokerApplication.salutDataReceiver = salutDataReceiver;
    }

    public static Salut getNetwork() {
        return network;
    }

    public static void setNetwork(Salut network) {
        PartyPokerApplication.network = network;
    }

    public static SalutServiceData getSalutServiceData() {
        return salutServiceData;
    }

    public static void setSalutServiceData(SalutServiceData salutServiceData) {
        PartyPokerApplication.salutServiceData = salutServiceData;
    }

    public static void addConnectedDevice(@NonNull SalutDevice device) {
        connectedDevices.add(device);
    }

    public static ArrayList<SalutDevice> getConnectedDevices() {
        return connectedDevices;
    }

    public static void resetConnectedDevices() {
        connectedDevices = new ArrayList<>();
    }
}
