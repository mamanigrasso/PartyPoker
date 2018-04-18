package at.aau.pokerfox.partypoker.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import at.aau.pokerfox.partypoker.R;

/**
 * Created by TimoS on 04.04.2018.
 */

public class GameActivity extends AppCompatActivity {


    //should contain the activePlayerNames
    private String[] nameOfPlayers = {
            "Player 1", "Player 2","Player 3","Player 4","Player 5","Player 6"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_table);


        //Dialog Created by Andreas on 18.04.2018
        Button btnShowCheater = (Button)findViewById(R.id.btn_cheating);
        btnShowCheater.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder createDialog = new AlertDialog.Builder(v.getContext());

                createDialog.setTitle("Choose the Cheater!");
                createDialog.setMessage("You have 5 seconds");
                createDialog.setItems(nameOfPlayers, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int indexPosition) {

                    }});
                createDialog.setNegativeButton("Cancel",null);

                createDialog.setCancelable(true);


                final AlertDialog chooseTheCheater= createDialog.create();

                chooseTheCheater.show();

                final Timer timeout = new Timer();

                //Timer to close after 5 seconds
                timeout.schedule(new TimerTask() {

                    public void run() {
                        chooseTheCheater.dismiss(); //timeout after 5 seconds
                        timeout.cancel(); //timer is canceled now
                    }
                }, 5000);
            }
        });
    }
}

