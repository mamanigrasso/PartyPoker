package at.aau.pokerfox.partypoker.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import android.widget.ImageView;
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

        //Dialog Created by Andreas
        Button btnShowCheater = (Button) findViewById(R.id.btn_cheating);

        btnShowCheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder createDialog = new AlertDialog.Builder(GameActivity.this);

                createDialog.setTitle("Choose the Cheater! You have 5 seconds");
                createDialog.setSingleChoiceItems(playerNames, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int indexPosition) {
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

    @Override
    protected void onResume()  {

        super.onResume();
       // turnMiddleCards();
       // turnOwnCards();
        turnForXPlayers(true, true, true,true,true,true,true,true,true,true);
    }

    public void turnForXPlayers(boolean player1, boolean player2, boolean player3,boolean player4,boolean player5, boolean flop1, boolean flop2, boolean flop3, boolean turn, boolean river) {
        // TODO: googlen ob nicht elegantere Variante in JAva möglich mit dynamischen arrays
        if (player1) {
            int[] myIds = {R.id.playerCard1, R.id.playerCard2};
            turnCards(myIds);
        }
        if (player1) {
            int[] myIds = {R.id.opponent1Card1, R.id.opponent1Card2};
            turnCards(myIds);
        }
        if (player2) {
            int[] myIds = {R.id.opponent2Card1, R.id.opponent2Card2};
            turnCards(myIds);
        }
        if (player3) {
            int[] myIds = {R.id.opponent3Card1, R.id.opponent3Card2};
            turnCards(myIds);
        }
        if (player4) {
            int[] myIds = {R.id.opponent4Card1, R.id.opponent4Card2};
            turnCards(myIds);
        }
        if (player5) {
            int[] myIds = {R.id.opponent5Card1, R.id.opponent5Card2};
            turnCards(myIds);
        }
        if (flop1) {
            int[] myIds = {R.id.flop1};
            turnCards(myIds);
        }
            if (flop2) {
                int[] myIds = {R.id.flop2};
                turnCards(myIds);
            }
            if (flop3) {
                int[] myIds = {R.id.flop3};
                turnCards(myIds);
            }
            if (turn) {
                int[] myIds = {R.id.turn};
                turnCards(myIds);
            }
            if (river) {
                int[] myIds = {R.id.river};
                turnCards(myIds);
            }

        // .. beliebig erweitern
    }
    public void turnMiddleCards() {
        int[] myIds = {R.id.flop1, R.id.flop2, R.id.flop3, R.id.turn, R.id.river};
        turnCards(myIds);
    }

    public void turnOwnCards() {
        int[] myIds = {R.id.playerCard1, R.id.playerCard2};
        turnCards(myIds);
    }

    public void turnCards(int[] viewIds) {
        if (viewIds == null)
            return;
        for (int viewId : viewIds) {
        final ImageView myView = findViewById(viewId);
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(myView, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(myView, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setImageResource(R.drawable.chips);

                oa2.start();
            }
        });
        oa1.start();
        }
    }
}

