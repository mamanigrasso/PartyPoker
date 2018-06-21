package at.aau.pokerfox.partypoker.model;

import android.os.AsyncTask;
import java.util.ArrayList;

/**
 * Created by Manuel on 25.04.2018.
 */

public class PlayerAction extends AsyncTask<Integer, Void, Integer> {
    private Player p;

    public PlayerAction(Player p) {
        this.p = p;
    }

    public int askForAction(int amount) {
        int returnAmount = amount;

        if (Math.random()*3 < Math.random()) {
            if (amount == 0) {
                if (p.getChipCount() > 60) {
                    returnAmount = 60;
                    System.out.println(p.getName() + " bet: " + returnAmount);
                } else {
                    returnAmount = p.getChipCount() + p.getCurrentBid();
                    System.out.println(p.getName() + " bet: " + returnAmount);
                }
            } else {
                if (p.getChipCount() > returnAmount * 2) {
                    returnAmount = amount * 2;
                    System.out.println(p.getName() + " raised: " + returnAmount);
                } else {
                    if (p.getChipCount() > 0) {
                        returnAmount = p.getChipCount() + p.getCurrentBid();
                        System.out.println(p.getName() + " raised: " + returnAmount);
                    } else
                        returnAmount = 0;
                }
            }
        }

        else if (Math.random()/2 > Math.random()) {
            returnAmount = p.getCurrentBid();
            System.out.println(p.getName() + " folded");

            return -1;
        } else {
            if (amount == 0)
                System.out.println(p.getName() + " checked");
            else {
                if (p.getChipCount() > amount)
                    returnAmount = amount;
                else {
                    returnAmount = p.getChipCount() + p.getCurrentBid();
                }
                System.out.println(p.getName() + " called: " + returnAmount);

            }
        }

        return returnAmount; // should be the amount specified by the player*/
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try {
            Thread.sleep(1000);
            int bid = askForAction(integers[0]);
            return bid;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer playerBid) {
        if (playerBid == -1)
            Game.getInstance().playerFolded();
        else
            Game.getInstance().playerBid(playerBid);
    }
}
