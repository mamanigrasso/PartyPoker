package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;

/**
 * Created by Manuel on 26.04.2018.
 */

public interface ModActInterface {
    public void showPlayerActions(int minAmountToRaise);
    public void hidePlayerActions();
    public void update(ArrayList<Card> communityCards, int potSize, ArrayList<Player> allPlayers);
    public void showWinner(String winnerInfo, boolean finalWinner);
}
