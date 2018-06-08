package at.aau.pokerfox.partypoker.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import at.aau.pokerfox.partypoker.model.CardDeck;
import at.aau.pokerfox.partypoker.model.CardListAdapter;
import at.aau.pokerfox.partypoker.model.DrawableCard;

/**
 * Created by Andreas on 28.05.2018.
 */


//This class on the one hand creates a new ArrayAdapter based on the "CardListAdapter-class"
    //On the other hand it fills up an ArrayList named "cardList" which contains allDrawableCards that we have in our Deck
    //Now the adapter uses this list for the creation of the adapter

//In the end this class gives the result back which is being catched by the method "onActivityResult" at the GameActivity.class


public class CardList_array_adapterActivity  extends ListActivity {


    public static String resultCardID = "deadMansCardID";
    public String[] cardnames;
    public String[] cardIDs;
    public ArrayList<Drawable> cardDrawableList;
    public ArrayList<DrawableCard> cardList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fillUpCardList();
        ArrayAdapter<DrawableCard> adapter = new CardListAdapter(this, cardList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawableCard d = cardList.get(position);

                Intent returnIntent = new Intent();

                returnIntent.putExtra(resultCardID,d.getCardID());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public void fillUpCardList() {

        cardnames = new String [52];
        cardIDs = new String [52];
        cardDrawableList = new ArrayList<>();
        cardList = new ArrayList<>();

        //Filling up with the CardNames
        for (int i = 0; i<52; i++) {
            cardnames[i]= getResources().getResourceEntryName(CardDeck.getDrawableIds().get(i)); //hopefully works
        }

        //Fillig up with the CardIDs
        for (int i = 0; i<52; i++) {
            cardIDs[i]=CardDeck.getDrawableIds().get(i).toString();
        }

        //Filling up with the DrawableObjects
        for (int i = 0; i<52; i++) {
            cardDrawableList.add(getDrawable(CardDeck.getDrawableIds().get(i)));
        }


        for(int i = 0; i < 52; i++){
            cardList.add(new DrawableCard(cardnames[i], cardIDs[i], cardDrawableList.get(i)));
        }

    }
}
