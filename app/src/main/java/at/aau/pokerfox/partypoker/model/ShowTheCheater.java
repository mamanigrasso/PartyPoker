package at.aau.pokerfox.partypoker.model;

import java.util.ArrayList;

/**
 * Created by Andreas on 18.04.2018.
 */

public class ShowTheCheater {

    public ShowTheCheater() {
    }

    /**
     * Finds out if somebody cheated and reduces the Chip-Count of the cheater or if the suspicion was wrong the
     * Chip-Count of the User will be reduced.
     * @param activePlayers - all current active Players
     * @param user - is the player who wants to know if another player cheated_so the user
     * @param cheater - is the player who is suspected of cheating
     * @param penalty - chips that will be reduced
     */

    // @ditHeCheat should be called after this sequence in class "Game";
    //
    // System.out.println("Current pot size: " + potSize);

    // LOC 54 inside the "for-Loop"

    public void ditHeCheat(ArrayList<Player> activePlayers, Player user, Player cheater, int penalty) {

        for (Player aktualPlayer : activePlayers) {
            if (cheater == aktualPlayer) {

                if (cheater.getCheatStatus() == false) {

                    if (user.getChipCount() >= penalty) {
                        user.reduceChipCount(penalty);
                        cheater.raiseChipCount(penalty);
                    } else {
                        cheater.raiseChipCount(user.getChipCount());
                        user.setChipCount(0);
                    }
                    break;

                } else if (cheater.getCheatStatus() == true) {

                    if (cheater.getChipCount() >= penalty) {
                        cheater.reduceChipCount(penalty);
                        user.raiseChipCount(penalty);
                    } else {
                        user.raiseChipCount(cheater.getChipCount());
                        cheater.setChipCount(0);
                    }
                    break;
                }
            }
        }
    }
}
