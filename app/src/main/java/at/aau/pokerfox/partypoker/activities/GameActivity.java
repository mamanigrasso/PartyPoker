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


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import at.aau.pokerfox.partypoker.R;
import at.aau.pokerfox.partypoker.model.Game;
import at.aau.pokerfox.partypoker.model.ModActInterface;
import at.aau.pokerfox.partypoker.model.Player;

/**
 * Created by TimoS on 04.04.2018.
 */

public class GameActivity extends AppCompatActivity implements Observer,ModActInterface {


    //should contain the activePlayerNames so the "getActivePlayer"-method of the GameClass
    //then player.name to find out the NameOfThePlayer -->this names push to this String[]
    private String[] playerNames = {
            "Marco", "Mathias","Timo","Michael","Manuel","Andreas"
    };

    boolean wasCheating = true; //findsOutIf the Player was cheating with player.cheatStatus
    private ArrayList<Player> players;

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

        if (true) {  // if is host
            startGame();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        updateViews();
    }

    private void startGame() {
        players = new ArrayList<Player>();

        Player player1 = new Player("Player1");
        players.add(player1);

        Player player2 = new Player("Player2");
        players.add(player2);

        Player player3 = new Player("Player3");
        players.add(player3);

        Player player4 = new Player("Player4");
        players.add(player4);

        Player player5 = new Player("Player5");
        players.add(player5);

        Player player6 = new Player("Player6");
        players.add(player6);

        Game.init(10, 10, 1000, 6, this);
        Game.addPlayer(player6);
        Game.addPlayer(player5);
        Game.addPlayer(player4);
        Game.addPlayer(player3);
        Game.addPlayer(player2);
        Game.addPlayer(player1);

        updateViews();
        setPlayerNames();

        Game.getInstance().addObserver(this);
        Game.getInstance().startRound();
    }

    private void updateViews() {
        TextView view1 = (TextView)findViewById(R.id.txt_chipsplayer);
        view1.setText(String.valueOf(players.get(0).getChipCount()));
        TextView view2 = (TextView)findViewById(R.id.txt_chipsop1);
        view2.setText(String.valueOf(players.get(1).getChipCount()));
        TextView view3 = (TextView)findViewById(R.id.txt_chipsop2);
        view3.setText(String.valueOf(players.get(2).getChipCount()));
        TextView view4 = (TextView)findViewById(R.id.txt_chipsop3);
        view4.setText(String.valueOf(players.get(3).getChipCount()));
        TextView view5 = (TextView)findViewById(R.id.txt_chipsop4);
        view5.setText(String.valueOf(players.get(4).getChipCount()));
        TextView view6 = (TextView)findViewById(R.id.txt_chipsop5);
        view6.setText(String.valueOf(players.get(5).getChipCount()));
        TextView view7 = (TextView)findViewById(R.id.txt_pot);
        view7.setText(String.valueOf(Game.getInstance().getPotSize()));
    }

    private void setPlayerNames() {
        TextView viewPlayer1 = (TextView)findViewById(R.id.txtPlayer);
        viewPlayer1.setText(players.get(0).getName());
        TextView viewPlayer2 = (TextView)findViewById(R.id.txtOpponent1);
        viewPlayer2.setText(players.get(1).getName());
        TextView viewPlayer3 = (TextView)findViewById(R.id.txtOpponent2);
        viewPlayer3.setText(players.get(2).getName());
        TextView viewPlayer4 = (TextView)findViewById(R.id.txtOpponent3);
        viewPlayer4.setText(players.get(3).getName());
        TextView viewPlayer5 = (TextView)findViewById(R.id.txtOpponent4);
        viewPlayer5.setText(players.get(4).getName());
        TextView viewPlayer6 = (TextView)findViewById(R.id.txtOpponent5);
        viewPlayer6.setText(players.get(5).getName());
    }

    @Override
    public void hidePlayerActions() {
        Button buttonCheck = (Button)findViewById(R.id.btn_check);
        Button buttonFold = (Button)findViewById(R.id.btn_fold);
        Button buttonRaise = (Button)findViewById(R.id.btn_raise);

        buttonCheck.setVisibility(View.GONE);
        buttonFold.setVisibility(View.GONE);
        buttonRaise.setVisibility(View.GONE);
    }

    @Override
    public void showPlayerActions(boolean bCheck) {
        Button buttonCheck = (Button)findViewById(R.id.btn_check);
        Button buttonFold = (Button)findViewById(R.id.btn_fold);
        Button buttonRaise = (Button)findViewById(R.id.btn_raise);

        buttonFold.setVisibility(View.VISIBLE);
        buttonRaise.setVisibility(View.VISIBLE);
        buttonCheck.setVisibility(View.VISIBLE);

        if (bCheck)
            buttonCheck.setText("CHECK");
        else
            buttonCheck.setText("CALL");
    }

    public void buttonCheckPressed(View v) {
        Button buttonCheck = (Button)findViewById(R.id.btn_check);

        if (buttonCheck.getText().toString().compareTo("CHECK") == 0)
            Game.getInstance().playerBid(0, false);
    }

    public void buttonFoldPressed(View v) {
        Game.getInstance().playerFolded();
    }

    public void buttonRaisePressed(View v) {
        Game.getInstance().playerBid(100, false);
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

