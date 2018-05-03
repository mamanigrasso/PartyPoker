package at.aau.pokerfox.partypoker.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import at.aau.pokerfox.partypoker.R;

/**
 * Created by TimoS on 04.04.2018.
 */

public class GameActivity extends AppCompatActivity {


    //should contain the activePlayerNames so the "getActivePlayer"-method of the GameClass
    //then player.name to find out the NameOfThePlayer -->this names push to this String[]
    private String[] playerNames = {
            "Marco", "Mathias","Timo","Michael","Manuel","Andreas"
    };

    boolean wasCheating = true; //findsOutIf the Player was cheating with player.cheatStatus

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_table);

        //Dialog Created by Andreas on 18.04.2018
        Button btnShowCheater = (Button) findViewById(R.id.btn_table4);
        //final TextView tvShowCheater = (TextView) findViewById(R.id.tvResult);

        btnShowCheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder createDialog = new AlertDialog.Builder(GameActivity.this);

                createDialog.setTitle("Choose the Cheater! You have 5 seconds");
                // createDialog.setMessage("You have 5 seconds");  //nicht aktivieren, sonst Funktioniert AllertDialog nicht
                createDialog.setSingleChoiceItems(playerNames, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int indexPosition) {
                       // tvShowCheater.setText(playerNames[indexPosition]);
                        dialogInterface.dismiss();
                        if (wasCheating==true) {  //Shows TOAST whether the player choose right or wrong - dependency to "wasCheating"
                            Toast.makeText(GameActivity.this, "You were right", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(GameActivity.this, "You were wrong", Toast.LENGTH_LONG).show();

                        }
                    }
                });
                createDialog.setNegativeButton("Cancel", null);

                createDialog.setCancelable(true);


                final AlertDialog chooseTheCheater = createDialog.create();  //Dialog is beeing created

                chooseTheCheater.show();

                final Timer timeoutDialog = new Timer();

                //Timer to close after 5 seconds
                timeoutDialog.schedule(new TimerTask() {

                    public void run() {
                        chooseTheCheater.dismiss(); //timeout after 5 seconds
                        timeoutDialog.cancel(); //timer is canceled now
                    }
                }, 5000);

            }

        });
    }
}

