package at.aau.pokerfox.partypoker.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import at.aau.pokerfox.partypoker.R;
import at.aau.pokerfox.partypoker.model.CardDeck;
import at.aau.pokerfox.partypoker.model.CardListAdapter;
import at.aau.pokerfox.partypoker.model.DrawableCard;

/**
 * Created by Andreas on 28.05.2018.
 */

public class CardList_array_adapterActivity  extends ListActivity {

    //public static String RESULT_CONTRYCODE = "countrycode";
    public static String resultCardID = "deadMansCardID";
    public String[] cardnames;
    public String[] cardIDs;
    public ArrayList<Drawable> cardDrawableList;
    public ArrayList<DrawableCard> cardList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //next 3 lines ?
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cardimages_row);*/

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
                //imgs.recycle(); //recycle images
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
            cardnames[i]= getResources().getResourceName(CardDeck.addDrawableIds().get(i)); //hopefully works
        }

        //Fillig up with the CardIDs
        for (int i = 0; i<52; i++) {
            cardIDs[i]=CardDeck.addDrawableIds().get(i).toString();
        }

        //Filling up with the DrawableObjects
        for (int i = 0; i<52; i++) {
            cardDrawableList.add(getDrawable(CardDeck.addDrawableIds().get(i)));
        }


        for(int i = 0; i < 52; i++){
            cardList.add(new DrawableCard(cardnames[i], cardIDs[i], cardDrawableList.get(i)));
        }

    }
}
