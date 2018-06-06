package at.aau.pokerfox.partypoker.model;

/**
 * Created by Manuel on 26.04.2018.
 */

public interface ModActInterface {
    public void showPlayerActions(int minAmountToRaise);
    public void hidePlayerActions();
    public void update();
    public void showAllPlayerCards();
}
