package at.aau.pokerfox.partypoker.model;


/**
 * Created by Andreas on 28.05.2018.
 */

public class DrawableCard {
    private String cardName;
    private String cardID;
    private int imageID;


    public DrawableCard(String cardName, String cardID, int imageID){

        this.cardName = cardName;
        this.cardID = cardID;
        this.imageID = imageID;
    }


    public String getName() {
        return cardName;
    }

    public int getImageID() {
        return imageID;
    }

    public String getCardID() {
        return cardID;
    }
}

