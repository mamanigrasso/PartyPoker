package at.aau.pokerfox.partypoker;

import android.app.Application;
import android.content.Context;

public class PartyPokerApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
