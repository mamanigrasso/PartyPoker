package at.aau.pokerfox.partypoker.model;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Manuel on 07.06.2018.
 */

public class ShowWinnerTask extends AsyncTask<Integer, Void, Integer> {

    public ShowWinnerTask() {

    }
    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            Log.e(e.getMessage(), "Error in ShowWinnerTask");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer playerBid) {
        Game.getInstance().startRound();
    }
}
