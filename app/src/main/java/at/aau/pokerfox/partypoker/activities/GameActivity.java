package at.aau.pokerfox.partypoker.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import at.aau.pokerfox.partypoker.model.Card;
import at.aau.pokerfox.partypoker.model.Game;
import at.aau.pokerfox.partypoker.model.network.messages.BroadcastKeys;
import at.aau.pokerfox.partypoker.model.network.messages.Broadcasts;

import static at.aau.pokerfox.partypoker.model.network.messages.Broadcasts.ACTION_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.messages.Broadcasts.INIT_GAME_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.messages.Broadcasts.NEW_CARD_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.messages.Broadcasts.PLAYER_ROLES_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.messages.Broadcasts.UPDATE_TABLE_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.messages.Broadcasts.WON_AMOUNT_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.messages.Broadcasts.YOUR_TURN_MESSAGE;

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
    private PokerBroadcastReceiver receiver;

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

        this.receiver = new PokerBroadcastReceiver();
    }

    @Override
    protected void onResume()  {

        super.onResume();
       // turnMiddleCards();
       // turnOwnCards();
        turnForXPlayers(true, true, true,true,true,true,true,true,true,true);

        registerForPokerBroadcasts(this.receiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.receiver);
    }

    public void turnForXPlayers(boolean player1, boolean player2, boolean player3, boolean player4, boolean player5, boolean flop1, boolean flop2, boolean flop3, boolean turn, boolean river) {
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

    private void registerForPokerBroadcasts(@NonNull PokerBroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(ACTION_MESSAGE);
        filter.addAction(Broadcasts.INIT_GAME_MESSAGE);
        filter.addAction(Broadcasts.UPDATE_TABLE_MESSAGE);
        filter.addAction(Broadcasts.YOUR_TURN_MESSAGE);
        filter.addAction(Broadcasts.PLAYER_ROLES_MESSAGE);
        filter.addAction(Broadcasts.NEW_CARD_MESSAGE);
        filter.addAction(Broadcasts.WON_AMOUNT_MESSAGE);
        registerReceiver(receiver, filter);
    }

    private void handleActionMessage(Bundle bundle) {
        int amount = bundle.getInt(BroadcastKeys.AMOUNT);
        boolean hasFolded = bundle.getBoolean(BroadcastKeys.HAS_FOLDED);
        boolean isAllIn = bundle.getBoolean(BroadcastKeys.IS_ALL_IN);

        if (hasFolded) {
            // Game.playerFolded();
        } else {
            // Game.playerBid(amount, isAllin);
        }
    }


    private void handleInitGameMessage(Bundle bundle) {}
    private void handlePlayerRolesMessage(Bundle bundle) {}

    private void handleNewCardMessage(Bundle bundle) {
        Card newHandCard = bundle.getParcelable(BroadcastKeys.CARD);

        // update cards UI
    }

    private void handleWonAmountMessage(Bundle bundle) {}
    private void handleUpdateTableMessage(Bundle bundle) {}
    private void handleYourTurnMessage(Bundle bundle) {}

    private class PokerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();

            switch (action) {
                case ACTION_MESSAGE:
                    handleActionMessage(extras);
                    break;
                case INIT_GAME_MESSAGE:
                    handleInitGameMessage(extras);
                    break;
                case PLAYER_ROLES_MESSAGE:
                    handlePlayerRolesMessage(extras);
                    break;
                case NEW_CARD_MESSAGE:
                    handleNewCardMessage(extras);
                    break;
                case WON_AMOUNT_MESSAGE:
                    handleWonAmountMessage(extras);
                    break;
                case UPDATE_TABLE_MESSAGE:
                    handleUpdateTableMessage(extras);
                    break;
                case YOUR_TURN_MESSAGE:
                    handleYourTurnMessage(extras);
                    break;

                    default:
            }
        }
    }
}

