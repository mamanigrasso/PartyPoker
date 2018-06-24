package at.aau.pokerfox.partypoker.activities;

import android.app.ListActivity;
import android.content.Intent;
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

public class CardListArrayAdapterActivity extends ListActivity {


    public final static String RESULT_CARD_ID = "deadMansCardID";
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

                returnIntent.putExtra(RESULT_CARD_ID,d.getCardID());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public void fillUpCardList() {

        String[] cardnames = new String[52];
        String[] cardIDs = new String[52];
        ArrayList<Integer> cardDrawableIdList = new ArrayList<>();
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
