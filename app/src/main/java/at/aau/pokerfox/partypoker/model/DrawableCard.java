package at.aau.pokerfox.partypoker.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Andreas on 28.05.2018.
 */

public class DrawableCard {
    private String cardName;
    private String cardID;
    private Drawable image;


    public DrawableCard(String cardName, String cardID, Drawable image){

        this.cardName = cardName;
        this.cardID = cardID;
        this.image = image;
    }


    public String getName() {
        return cardName;
    }

    public Drawable getImage() {
        return image;
    }

    public String getCardID() {
        return cardID;
    }
}

