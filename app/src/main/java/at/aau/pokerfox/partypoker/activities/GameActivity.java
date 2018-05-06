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


import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.R;
import at.aau.pokerfox.partypoker.model.Game;
import at.aau.pokerfox.partypoker.model.ModActInterface;
import at.aau.pokerfox.partypoker.model.Player;
import at.aau.pokerfox.partypoker.model.Card;
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

public class GameActivity extends AppCompatActivity implements Observer,ModActInterface {


    //should contain the activePlayerNames so the "getActivePlayer"-method of the GameClass
    //then player.name to find out the NameOfThePlayer -->this names push to this String[]
    private String[] playerNames = {
            "Marco", "Mathias","Timo","Michael","Manuel","Andreas"
    };

    boolean wasCheating = true; //findsOutIf the Player was cheating with player.cheatStatus
    private ArrayList<Player> players;
    private PokerBroadcastReceiver receiver;
    private int bigBlind, playerPot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_table);

        if (PartyPokerApplication.isHost()) {
            Bundle bundle = getIntent().getExtras();
            bigBlind = bundle.getInt(HostGameActivity.BUNDLE_BIG_BLIND);
            playerPot = bundle.getInt(HostGameActivity.BUNDLE_PLAYER_POT);
        }

        Button btnShowCheater = findViewById(R.id.btn_cheating);

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

        if (PartyPokerApplication.isHost()) {  // if is host
            startGame();
        }
      
        this.receiver = new PokerBroadcastReceiver();
    }

    @Override
    public void update(Observable observable, Object o) {
        updateViews();
    }

    private void startGame() {
        players = new ArrayList<Player>();

        Player myself = new Player("host");
        players.add(myself);

        for (SalutDevice device : PartyPokerApplication.getConnectedDevices()) {
            Player p = new Player(device.instanceName);
            players.add(p);
        }

//        Player player1 = new Player("Player1");
//        players.add(player1);
//
//        Player player2 = new Player("Player2");
//        players.add(player2);
//
//        Player player3 = new Player("Player3");
//        players.add(player3);
//
//        Player player4 = new Player("Player4");
//        players.add(player4);
//
//        Player player5 = new Player("Player5");
//        players.add(player5);
//
//        Player player6 = new Player("Player6");
//        players.add(player6);

//        Game.init(10, 10, 1000, 6, this);
//        Game.addPlayer(player6);
//        Game.addPlayer(player5);
//        Game.addPlayer(player4);
//        Game.addPlayer(player3);
//        Game.addPlayer(player2);
//        Game.addPlayer(player1);

        Game.init(bigBlind, bigBlind, playerPot, players.size(), this);

        updateViews();
        setPlayerNames();

        Game.getInstance().addObserver(this);
        Game.getInstance().startRound();

        for (Player p : players) {
            Game.addPlayer(p);
        }
    }

    private void updateViews() {
        TextView view1 = (TextView)findViewById(R.id.txt_chipsplayer);
        view1.setText(String.valueOf(players.get(0).getChipCount()));
        TextView view2 = (TextView)findViewById(R.id.txt_chipsop1);
        view2.setText(String.valueOf(players.get(1).getChipCount()));
//        TextView view3 = (TextView)findViewById(R.id.txt_chipsop2);
//        view3.setText(String.valueOf(players.get(2).getChipCount()));
//        TextView view4 = (TextView)findViewById(R.id.txt_chipsop3);
//        view4.setText(String.valueOf(players.get(3).getChipCount()));
//        TextView view5 = (TextView)findViewById(R.id.txt_chipsop4);
//        view5.setText(String.valueOf(players.get(4).getChipCount()));
//        TextView view6 = (TextView)findViewById(R.id.txt_chipsop5);
//        view6.setText(String.valueOf(players.get(5).getChipCount()));
        TextView view7 = (TextView)findViewById(R.id.txt_pot);
        view7.setText(String.valueOf(Game.getInstance().getPotSize()));
    }

    private void setPlayerNames() {
        TextView viewPlayer1 = (TextView)findViewById(R.id.txtPlayer);
        viewPlayer1.setText(players.get(0).getName());
        TextView viewPlayer2 = (TextView)findViewById(R.id.txtOpponent1);
        viewPlayer2.setText(players.get(1).getName());
//        TextView viewPlayer3 = (TextView)findViewById(R.id.txtOpponent2);
//        viewPlayer3.setText(players.get(2).getName());
//        TextView viewPlayer4 = (TextView)findViewById(R.id.txtOpponent3);
//        viewPlayer4.setText(players.get(3).getName());
//        TextView viewPlayer5 = (TextView)findViewById(R.id.txtOpponent4);
//        viewPlayer5.setText(players.get(4).getName());
//        TextView viewPlayer6 = (TextView)findViewById(R.id.txtOpponent5);
//        viewPlayer6.setText(players.get(5).getName());
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
        //turnForXPlayers(true, true, true,true,true,true,true,true,true,true);
        getPlayerCards();
        getRiverCard();
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
      
//getting the needed Cards from the Class "Card" and link the image with the GUI
    public void getPlayerCards () {
        int [] playerCards = {R.id.playerCard1, R.id.playerCard2};
        getCards(playerCards);
    }

    public void getfirstThreeCommunityCards() {
        int [] firstThreeCommunityCards = {R.id.flop1, R.id.flop2, R.id.flop3};
        getCards(firstThreeCommunityCards);
    }

    public void getTurnCard() {
        int[] getTurn={R.id.turn};
        getCards(getTurn);
    }

    public void getRiverCard() {
        int[] getRiver ={R.id.river};
        getCards(getRiver);
    }

    public void getCards (int [] CardIDs) {
//        for(int i : CardIDs) {
//            final ImageView cardView = findViewById(i);
//            cardView.setImageDrawable(getDrawable(Card.getCards()));
//        }
    }
}

