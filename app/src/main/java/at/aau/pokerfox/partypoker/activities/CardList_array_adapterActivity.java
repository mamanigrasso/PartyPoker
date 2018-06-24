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

public class CardList_array_adapterActivity  extends ListActivity {


    public final static String resultCardID = "deadMansCardID";
    private String[] cardnames;
    private String[] cardIDs;
    private ArrayList<Integer> cardDrawableIdList;
    private ArrayList<DrawableCard> cardList;


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
        cardDrawableIdList = new ArrayList<>();
        cardList = new ArrayList<>();

        for (int i = 0; i<52; i++) {
            cardnames[i]= getResources().getResourceEntryName(CardDeck.getDrawableIds().get(i));
        }

        for (int i = 0; i<52; i++) {
            cardIDs[i]=CardDeck.getDrawableIds().get(i).toString();
        }

        for (int i = 0; i<52; i++) {
            cardDrawableIdList.add(CardDeck.getDrawableIds().get(i));
        }


        for(int i = 0; i < 52; i++){
            cardList.add(new DrawableCard(cardnames[i], cardIDs[i], cardDrawableIdList.get(i)));
        }

    }
}
