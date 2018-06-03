package at.aau.pokerfox.partypoker.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.peak.salut.SalutDevice;

import java.io.Console;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.R;
import at.aau.pokerfox.partypoker.model.CardDeck;
import at.aau.pokerfox.partypoker.model.CardListAdapter;
import at.aau.pokerfox.partypoker.model.DrawableCard;
import at.aau.pokerfox.partypoker.model.ShowTheCheater;
import at.aau.pokerfox.partypoker.model.Game;
import at.aau.pokerfox.partypoker.model.ModActInterface;
import at.aau.pokerfox.partypoker.model.Player;
import at.aau.pokerfox.partypoker.model.Card;
import at.aau.pokerfox.partypoker.model.network.BroadcastKeys;
import at.aau.pokerfox.partypoker.model.network.Broadcasts;
import at.aau.pokerfox.partypoker.model.network.messages.client.ActionMessage;

import static at.aau.pokerfox.partypoker.model.network.Broadcasts.ACTION_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.INIT_GAME_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.NEW_CARD_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.PLAYER_ROLES_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.UPDATE_TABLE_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.WON_AMOUNT_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.YOUR_TURN_MESSAGE;

/**
 * Created by TimoS on 04.04.2018.
 */

public class GameActivity extends AppCompatActivity implements Observer,ModActInterface {



    private String[] playerNames=new String[6];
            //= {"Marco", "Mathias","Timo","Michael","Manuel","Andreas"};


    private boolean isCheatingAllowed = true; //initGameMessage - METHOD
    private ShowTheCheater showTheCheater;
    int bigBlind;
    private ArrayList<Player> players;
    private ArrayList<Card> communityCards = new ArrayList<>();
    private PokerBroadcastReceiver receiver;
    private int playerPot;
    private int minAmountToRaise;
    private int potSize = 0;
    private boolean eyePossible = false;
    private TextView tvTablePot;

    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;
    private TextView tvPlayer3Name;
    private TextView tvPlayer4Name;
    private TextView tvPlayer5Name;
    private TextView tvPlayer6Name;

    private TextView tvPlayer1Chips;
    private TextView tvPlayer2Chips;
    private TextView tvPlayer3Chips;
    private TextView tvPlayer4Chips;
    private TextView tvPlayer5Chips;
    private TextView tvPlayer6Chips;

    private TextView tvPlayer1Bid;
    private TextView tvPlayer2Bid;
    private TextView tvPlayer3Bid;
    private TextView tvPlayer4Bid;
    private TextView tvPlayer5Bid;
    private TextView tvPlayer6Bid;

    private ImageView ivPlayer1BigBlind;
    private ImageView ivPlayer2BigBlind;
    private ImageView ivPlayer3BigBlind;
    private ImageView ivPlayer4BigBlind;
    private ImageView ivPlayer5BigBlind;
    private ImageView ivPlayer6BigBlind;

    private ImageView ivPlayer1SmallBlind;
    private ImageView ivPlayer2SmallBlind;
    private ImageView ivPlayer3SmallBlind;
    private ImageView ivPlayer4SmallBlind;
    private ImageView ivPlayer5SmallBlind;
    private ImageView ivPlayer6SmallBlind;

    private ImageView ivPlayer1Dealer;
    private ImageView ivPlayer2Dealer;
    private ImageView ivPlayer3Dealer;
    private ImageView ivPlayer4Dealer;
    private ImageView ivPlayer5Dealer;
    private ImageView ivPlayer6Dealer;

    private TextView tvPlayer1Status;
    private TextView tvPlayer2Status;
    private TextView tvPlayer3Status;
    private TextView tvPlayer4Status;
    private TextView tvPlayer5Status;
    private TextView tvPlayer6Status;

    private ImageView ivPlayer1Card1;
    private ImageView ivPlayer1Card2;
    private ImageView ivPlayer2Card1;
    private ImageView ivPlayer2Card2;
    private ImageView ivPlayer3Card1;
    private ImageView ivPlayer3Card2;
    private ImageView ivPlayer4Card1;
    private ImageView ivPlayer4Card2;
    private ImageView ivPlayer5Card1;
    private ImageView ivPlayer5Card2;
    private ImageView ivPlayer6Card1;
    private ImageView ivPlayer6Card2;


    private SeekBar sbRaisedAmount;
    private ImageView ivTableCard1;
    private ImageView ivTableCard2;
    private ImageView ivTableCard3;
    private ImageView ivTableCard4;
    private ImageView ivTableCard5;

    private Button btnCheat;
    private Button btnShowTableCard;
    private Button btnProbability;
    private Button btnChooseOneCardFromDeck;


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


        createAllViews();

        if (PartyPokerApplication.isHost()) {  // if is host
            startGame();
        }



        this.receiver = new PokerBroadcastReceiver();




        createAllViews();
        hideAllViews();

        if (PartyPokerApplication.isHost()) {  // if is host
            startGame();
        }

        showTheCheater();
        initialiseCheatButtons();
        hideCheatButtons();
        setCheatButtonsVisible();
        chooseOneCardFromDeck();
      
        this.receiver = new PokerBroadcastReceiver();

        this.sbRaisedAmount = findViewById(R.id.seekBar2);

        hidePlayerActions();
    }



    @Override
    public void update(Observable observable, Object o) {
        updateViews();
    }

    private void startGame() {
        players = new ArrayList<>();

        Player myself = new Player("host");
        myself.setIsHost(true);
        players.add(myself);

        //TODO: find out real player count
        Game.init(bigBlind, bigBlind, playerPot, 2, this);

        Game.addPlayer(myself);

        for (SalutDevice device : PartyPokerApplication.getConnectedDevices()) {
            Player p = new Player(device.instanceName);
            p.setDevice(device);
            players.add(p);
            Game.addPlayer(p);
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

        Game.getInstance().initGame();
//        Game.init(10, 10, 1000, 6, this);

//        Game.addPlayer(player6);
//        Game.addPlayer(player5);
//        Game.addPlayer(player4);
//        Game.addPlayer(player3);
//        Game.addPlayer(player2);
//        Game.addPlayer(player1);

       // player1.setChipCount(650);
        // player2.setChipCount(700);
       // player2.setCheatStaus(true);

        setPlayerNames();

        Game.getInstance().addObserver(this);
        Game.getInstance().startRound();
        eyePossible = false;
        updateViews();
    }
  
    private void createTablePotView() {
        tvTablePot = findViewById(R.id.txt_pot);
    }

    private void updateTablePot() {
        tvTablePot.setText(String.valueOf(potSize));
    }

    private void createPlayerNameViews() {
        tvPlayer1Name = findViewById(R.id.txtPlayer);
        tvPlayer2Name = findViewById(R.id.txtOpponent1);
        tvPlayer3Name = findViewById(R.id.txtOpponent2);
        tvPlayer4Name = findViewById(R.id.txtOpponent3);
        tvPlayer5Name = findViewById(R.id.txtOpponent4);
        tvPlayer6Name = findViewById(R.id.txtOpponent5);
    }

    private void setPlayerNames() {
        tvPlayer1Name.setText(players.get(0).getName());
        tvPlayer2Name.setText(players.get(1).getName());
//        tvPlayer3Name.setText(players.get(2).getName());
//        tvPlayer4Name.setText(players.get(3).getName());
//        tvPlayer5Name.setText(players.get(4).getName());
//        tvPlayer6Name.setText(players.get(5).getName());
    }

    private void createPlayerChipsViews() {
        tvPlayer1Chips = findViewById(R.id.txt_chipsplayer);
        tvPlayer2Chips = findViewById(R.id.txt_chipsop1);
        tvPlayer3Chips = findViewById(R.id.txt_chipsop2);
        tvPlayer4Chips = findViewById(R.id.txt_chipsop3);
        tvPlayer5Chips = findViewById(R.id.txt_chipsop4);
        tvPlayer6Chips = findViewById(R.id.txt_chipsop5);
    }

    private void updatePlayerChipsViews() {
        createPlayerChipsViews();
        tvPlayer1Chips.setText(String.valueOf(players.get(0).getChipCount()));
        tvPlayer2Chips.setText(String.valueOf(players.get(1).getChipCount()));
//        tvPlayer3Chips.setText(String.valueOf(players.get(2).getChipCount()));
//        tvPlayer4Chips.setText(String.valueOf(players.get(3).getChipCount()));
//        tvPlayer5Chips.setText(String.valueOf(players.get(4).getChipCount()));
//        tvPlayer6Chips.setText(String.valueOf(players.get(5).getChipCount()));
    }

    private void createPlayerBidViews() {
        tvPlayer1Bid = findViewById(R.id.gebotself);
        tvPlayer2Bid = findViewById(R.id.gebotop1);
        tvPlayer3Bid = findViewById(R.id.gebotop2);
        tvPlayer4Bid = findViewById(R.id.gebotop3);
        tvPlayer5Bid = findViewById(R.id.gebotop4);
        tvPlayer6Bid = findViewById(R.id.gebotop5);
    }

    private void updatePlayerBidViews() {
        tvPlayer1Bid.setText(String.valueOf(players.get(0).getCurrentBid()));
        tvPlayer2Bid.setText(String.valueOf(players.get(1).getCurrentBid()));
//        tvPlayer3Bid.setText(String.valueOf(players.get(2).getCurrentBid()));
//        tvPlayer4Bid.setText(String.valueOf(players.get(3).getCurrentBid()));
//        tvPlayer5Bid.setText(String.valueOf(players.get(4).getCurrentBid()));
//        tvPlayer6Bid.setText(String.valueOf(players.get(5).getCurrentBid()));
    }

    private void createPlayerRoleViews() {
        ivPlayer1BigBlind = findViewById(R.id.img_bigblindopself);
        ivPlayer2BigBlind = findViewById(R.id.img_bigblindop1);
        ivPlayer3BigBlind = findViewById(R.id.img_bigblindop2);
        ivPlayer4BigBlind = findViewById(R.id.img_bigblindop3);
        ivPlayer5BigBlind = findViewById(R.id.img_bigblindop4);
        ivPlayer6BigBlind = findViewById(R.id.img_bigblindop5);

        ivPlayer1SmallBlind = findViewById(R.id.img_smallblindself);
        ivPlayer2SmallBlind = findViewById(R.id.img_smallblindop1);
        ivPlayer3SmallBlind = findViewById(R.id.img_smallblindop2);
        ivPlayer4SmallBlind = findViewById(R.id.img_smallblindop3);
        ivPlayer5SmallBlind = findViewById(R.id.img_smallblindop4);
        ivPlayer6SmallBlind = findViewById(R.id.img_smallblindop5);

        ivPlayer1Dealer = findViewById(R.id.img_dealerbtnself);
        ivPlayer2Dealer = findViewById(R.id.img_dealerbtnop1);
        ivPlayer3Dealer = findViewById(R.id.img_dealerbtnop2);
        ivPlayer4Dealer = findViewById(R.id.img_dealerbtnop3);
        ivPlayer5Dealer = findViewById(R.id.img_dealerbtnop4);
        ivPlayer6Dealer = findViewById(R.id.img_dealerbtnop5);
    }

    private void updatePlayerRoleViews() {
        ivPlayer1BigBlind.setVisibility(players.get(0).isBigBlind() ? View.VISIBLE : View.INVISIBLE);
        ivPlayer2BigBlind.setVisibility(players.get(1).isBigBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer3BigBlind.setVisibility(players.get(2).isBigBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer4BigBlind.setVisibility(players.get(3).isBigBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer5BigBlind.setVisibility(players.get(4).isBigBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer6BigBlind.setVisibility(players.get(5).isBigBlind() ? View.VISIBLE : View.INVISIBLE);

        ivPlayer1SmallBlind.setVisibility(players.get(0).isSmallBlind() ? View.VISIBLE : View.INVISIBLE);
        ivPlayer2SmallBlind.setVisibility(players.get(1).isSmallBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer3SmallBlind.setVisibility(players.get(2).isSmallBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer4SmallBlind.setVisibility(players.get(3).isSmallBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer5SmallBlind.setVisibility(players.get(4).isSmallBlind() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer6SmallBlind.setVisibility(players.get(5).isSmallBlind() ? View.VISIBLE : View.INVISIBLE);

        ivPlayer1Dealer.setVisibility(players.get(0).isDealer() ? View.VISIBLE : View.INVISIBLE);
        ivPlayer2Dealer.setVisibility(players.get(1).isDealer() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer3Dealer.setVisibility(players.get(2).isDealer() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer4Dealer.setVisibility(players.get(3).isDealer() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer5Dealer.setVisibility(players.get(4).isDealer() ? View.VISIBLE : View.INVISIBLE);
//        ivPlayer6Dealer.setVisibility(players.get(5).isDealer() ? View.VISIBLE : View.INVISIBLE);
    }

    private void createPlayerStatusViews() {
        tvPlayer1Status = findViewById(R.id.text_checkself);
        tvPlayer2Status = findViewById(R.id.text_checkop1);
        tvPlayer3Status = findViewById(R.id.text_checkop2);
        tvPlayer4Status = findViewById(R.id.text_checkop3);
        tvPlayer5Status = findViewById(R.id.text_checkop4);
        tvPlayer6Status = findViewById(R.id.text_checkop5);
    }

    private void createPlayerCards() {
        ivPlayer1Card1 = findViewById(R.id.playerCard1);
        ivPlayer1Card2 = findViewById(R.id.playerCard2);

        ivPlayer2Card1 = findViewById(R.id.opponent1Card1);
        ivPlayer2Card2 = findViewById(R.id.opponent1Card2);

        ivPlayer3Card1 = findViewById(R.id.opponent2Card1);
        ivPlayer3Card2 = findViewById(R.id.opponent2Card2);

        ivPlayer4Card1 = findViewById(R.id.opponent3Card1);
        ivPlayer4Card2 = findViewById(R.id.opponent3Card2);

        ivPlayer5Card1 = findViewById(R.id.opponent4Card1);
        ivPlayer5Card2 = findViewById(R.id.opponent4Card2);

        ivPlayer6Card1 = findViewById(R.id.opponent5Card1);
        ivPlayer6Card2 = findViewById(R.id.opponent5Card2);
    }

    private void updatePlayerStatusViews() {
        tvPlayer1Status.setText(String.valueOf(players.get(0).getStatus()));
        tvPlayer2Status.setText(String.valueOf(players.get(1).getStatus()));
//        tvPlayer3Status.setText(String.valueOf(players.get(2).getStatus()));
//        tvPlayer4Status.setText(String.valueOf(players.get(3).getStatus()));
//        tvPlayer5Status.setText(String.valueOf(players.get(4).getStatus()));
//        tvPlayer6Status.setText(String.valueOf(players.get(5).getStatus()));
    }

    private void createPlayerCardViews() {
        ivPlayer1Card1 = findViewById(R.id.playerCard1);
        ivPlayer1Card2 = findViewById(R.id.playerCard2);
        ivPlayer2Card1 = findViewById(R.id.opponent1Card1);
        ivPlayer2Card2 = findViewById(R.id.opponent1Card2);
        ivPlayer3Card1 = findViewById(R.id.opponent2Card1);
        ivPlayer3Card2 = findViewById(R.id.opponent2Card2);
        ivPlayer4Card1 = findViewById(R.id.opponent3Card1);
        ivPlayer4Card2 = findViewById(R.id.opponent3Card2);
        ivPlayer5Card1 = findViewById(R.id.opponent4Card1);
        ivPlayer5Card2 = findViewById(R.id.opponent4Card2);
        ivPlayer6Card1 = findViewById(R.id.opponent5Card1);
        ivPlayer6Card2 = findViewById(R.id.opponent5Card2);
    }

    private void updatePlayerCardViews() {
        ivPlayer1Card1.setVisibility(players.get(0).getCard1() == null ? View.INVISIBLE : View.VISIBLE);
        ivPlayer1Card2.setVisibility(players.get(0).getCard2() == null ? View.INVISIBLE : View.VISIBLE);
        ivPlayer2Card1.setVisibility(players.get(1).getCard1() == null ? View.INVISIBLE : View.VISIBLE);
        ivPlayer2Card2.setVisibility(players.get(1).getCard2() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer3Card1.setVisibility(players.get(2).getCard1() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer3Card2.setVisibility(players.get(2).getCard2() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer4Card1.setVisibility(players.get(3).getCard1() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer4Card2.setVisibility(players.get(3).getCard2() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer5Card1.setVisibility(players.get(4).getCard1() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer5Card2.setVisibility(players.get(4).getCard2() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer6Card1.setVisibility(players.get(5).getCard1() == null ? View.INVISIBLE : View.VISIBLE);
//        ivPlayer6Card2.setVisibility(players.get(5).getCard2() == null ? View.INVISIBLE : View.VISIBLE);

        if (players.get(0).getCard1() != null)
            ivPlayer1Card1.setImageDrawable(getDrawable(players.get(0).getCard1().getDrawableID()));
        if (players.get(0).getCard2() != null)
            ivPlayer1Card2.setImageDrawable(getDrawable(players.get(0).getCard2().getDrawableID()));

        if (players.get(1).getCard1() != null)
            ivPlayer2Card1.setImageDrawable(getDrawable(R.drawable.card_back));
        if (players.get(1).getCard2() != null)
            ivPlayer2Card2.setImageDrawable(getDrawable(R.drawable.card_back));

//        if (players.get(2).getCard1() != null)
//            ivPlayer3Card1.setImageDrawable(getDrawable(R.drawable.card_back));
//        if (players.get(2).getCard2() != null)
//            ivPlayer3Card2.setImageDrawable(getDrawable(R.drawable.card_back));
//
//        if (players.get(3).getCard1() != null)
//            ivPlayer4Card1.setImageDrawable(getDrawable(R.drawable.card_back));
//        if (players.get(3).getCard2() != null)
//            ivPlayer4Card2.setImageDrawable(getDrawable(R.drawable.card_back));
//
//        if (players.get(4).getCard1() != null)
//            ivPlayer5Card1.setImageDrawable(getDrawable(R.drawable.card_back));
//        if (players.get(4).getCard2() != null)
//            ivPlayer5Card2.setImageDrawable(getDrawable(R.drawable.card_back));
//
//        if (players.get(5).getCard1() != null)
//            ivPlayer6Card1.setImageDrawable(getDrawable(R.drawable.card_back));
//        if (players.get(5).getCard2() != null)
//            ivPlayer6Card2.setImageDrawable(getDrawable(R.drawable.card_back));
    }

    private void createTableCardViews() {
        ivTableCard1 = findViewById(R.id.flop1);
        ivTableCard2 = findViewById(R.id.flop2);
        ivTableCard3 = findViewById(R.id.flop3);
        ivTableCard4 = findViewById(R.id.turn);
        ivTableCard5 = findViewById(R.id.river);
    }

    private void updateTableCardViews() {

        if (this.communityCards.size() > 0) {
            ivTableCard1.setVisibility(View.VISIBLE);
            ivTableCard1.setImageDrawable(getDrawable((this.communityCards.get(0).getDrawableID())));
        }
        else
            ivTableCard1.setVisibility(View.INVISIBLE);

        if (this.communityCards.size() > 1) {
            ivTableCard2.setVisibility(View.VISIBLE);
            ivTableCard2.setImageDrawable(getDrawable((this.communityCards.get(1).getDrawableID())));
        }
        else
            ivTableCard2.setVisibility(View.INVISIBLE);

        if (this.communityCards.size() > 2) {
            ivTableCard3.setVisibility(View.VISIBLE);
            ivTableCard3.setImageDrawable(getDrawable((this.communityCards.get(2).getDrawableID())));
        }
        else
            ivTableCard3.setVisibility(View.INVISIBLE);

        if (this.communityCards.size() > 3) {
            ivTableCard4.setVisibility(View.VISIBLE);
            ivTableCard4.setImageDrawable(getDrawable((this.communityCards.get(3).getDrawableID())));
        }
        else
            ivTableCard4.setVisibility(View.INVISIBLE);

        if (this.communityCards.size() > 4) {
            ivTableCard5.setVisibility(View.VISIBLE);
            ivTableCard5.setImageDrawable(getDrawable((this.communityCards.get(4).getDrawableID())));
        }
        else
            ivTableCard5.setVisibility(View.INVISIBLE);

    }

    private void createAllViews() {
        createTablePotView();
        createPlayerNameViews();
        createPlayerChipsViews();
        createPlayerBidViews();
        createPlayerRoleViews();
        createPlayerStatusViews();
        createPlayerCards();
        createTableCardViews();
    }
  
    private void hideAllViews() {
        tvPlayer3Bid.setVisibility(View.INVISIBLE);
        tvPlayer3Chips.setVisibility(View.INVISIBLE);
        tvPlayer3Name.setVisibility(View.INVISIBLE);
        tvPlayer3Status.setVisibility(View.INVISIBLE);

        tvPlayer4Bid.setVisibility(View.INVISIBLE);
        tvPlayer4Chips.setVisibility(View.INVISIBLE);
        tvPlayer4Name.setVisibility(View.INVISIBLE);
        tvPlayer4Status.setVisibility(View.INVISIBLE);

        tvPlayer5Bid.setVisibility(View.INVISIBLE);
        tvPlayer5Chips.setVisibility(View.INVISIBLE);
        tvPlayer5Name.setVisibility(View.INVISIBLE);
        tvPlayer5Status.setVisibility(View.INVISIBLE);

        tvPlayer6Bid.setVisibility(View.INVISIBLE);
        tvPlayer6Chips.setVisibility(View.INVISIBLE);
        tvPlayer6Name.setVisibility(View.INVISIBLE);
        tvPlayer6Status.setVisibility(View.INVISIBLE);

        ivPlayer3Card1.setVisibility(View.INVISIBLE);
        ivPlayer3Card2.setVisibility(View.INVISIBLE);

        ivPlayer4Card1.setVisibility(View.INVISIBLE);
        ivPlayer4Card2.setVisibility(View.INVISIBLE);

        ivPlayer5Card1.setVisibility(View.INVISIBLE);
        ivPlayer5Card2.setVisibility(View.INVISIBLE);

        ivPlayer6Card1.setVisibility(View.INVISIBLE);
        ivPlayer6Card2.setVisibility(View.INVISIBLE);
    }

    private void updateViews() {
        updateTablePot();
        updatePlayerChipsViews();
        updatePlayerBidViews();
        updatePlayerRoleViews();
        updatePlayerStatusViews();
        updatePlayerCardViews();
        updateTableCardViews();
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

        Log.e("flow test", "in showPlayerActions()");

        buttonFold.setVisibility(View.VISIBLE);
        buttonRaise.setVisibility(View.VISIBLE);
        buttonCheck.setVisibility(View.VISIBLE);

        if (bCheck)
            buttonCheck.setText("CHECK");
        else
            buttonCheck.setText("CALL");
    }

    private void prepareAndSendActionMessage(int amount, boolean hasFolded, boolean isAllIn) {
        hidePlayerActions();
        ActionMessage message = new ActionMessage();
        message.Amount = amount;
        message.HasFolded = hasFolded;
        message.IsAllIn = isAllIn;
        showPlayerActions(false);
        PartyPokerApplication.getMessageHandler().sendMessageToHost(message);
    }

    public void buttonCheckPressed(View v) {
        //TODO: find out if player is all in
        boolean isAllIn = false;
        prepareAndSendActionMessage(this.minAmountToRaise, false, isAllIn);
    }
  
   @Override
    public void update() {
       updateViews();
    }

    @Override
    public void showAllPlayerCards() {
        if (players.get(1).getCard1() != null)
            ivPlayer2Card1.setImageDrawable(getDrawable(players.get(1).getCard1().getDrawableID()));
        if (players.get(1).getCard2() != null)
            ivPlayer2Card2.setImageDrawable(getDrawable(players.get(1).getCard2().getDrawableID()));

//        if (players.get(2).getCard1() != null)
//            ivPlayer3Card1.setImageDrawable(getDrawable(players.get(2).getCard1().getDrawableID()));
//        if (players.get(2).getCard2() != null)
//            ivPlayer3Card2.setImageDrawable(getDrawable(players.get(2).getCard2().getDrawableID()));
//
//        if (players.get(3).getCard1() != null)
//            ivPlayer4Card1.setImageDrawable(getDrawable(players.get(3).getCard1().getDrawableID()));
//        if (players.get(3).getCard2() != null)
//            ivPlayer4Card2.setImageDrawable(getDrawable(players.get(3).getCard2().getDrawableID()));
//
//        if (players.get(4).getCard1() != null)
//            ivPlayer5Card1.setImageDrawable(getDrawable(players.get(4).getCard1().getDrawableID()));
//        if (players.get(4).getCard2() != null)
//            ivPlayer5Card2.setImageDrawable(getDrawable(players.get(4).getCard2().getDrawableID()));
//
//        if (players.get(5).getCard1() != null)
//            ivPlayer6Card1.setImageDrawable(getDrawable(players.get(5).getCard1().getDrawableID()));
//        if (players.get(5).getCard2() != null)
//            ivPlayer6Card2.setImageDrawable(getDrawable(players.get(5).getCard2().getDrawableID()));
    }

    public void buttonFoldPressed(View v) {
        prepareAndSendActionMessage(0, true, false);
    }

    public void buttonEyePressed(View v) {
        //prepareAndSendActionMessage(0, true, false);
        if (eyePossible) {
            turnForXPlayers(false, false, false, false, false, false, false, false, false, true);
        }
    }

    public void buttonRaisePressed(View v) {
        int amount = this.sbRaisedAmount.getProgress();
        //TODO: find out if player is all in
        boolean isAllIn = false;
        prepareAndSendActionMessage(amount, false, isAllIn);

        //Game.getInstance().playerBid(120);
    }

    @Override
    protected void onResume()  {

        super.onResume();
       // turnMiddleCards();
       // turnOwnCards();
        //turnForXPlayers(true, true, true,true,true,true,true,true,true,true);
        //drawPlayerCards();
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
                eyePossible = true;
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
            Game.getInstance().playerFolded();
        } else {
            Game.getInstance().playerBid(amount);
        }
    }

    private void handleInitGameMessage(Bundle bundle) {
        ArrayList<Player> players = bundle.getParcelableArrayList(BroadcastKeys.PLAYERS);
        boolean isCheatingAllowed = bundle.getBoolean(BroadcastKeys.CHEAT_ON);
        int bigBlind = bundle.getInt(BroadcastKeys.BIG_BLIND);
        int playerPot = bundle.getInt(BroadcastKeys.PLAYER_POT);

        this.players = players;
        setPlayerNames();

        this.bigBlind = bigBlind;
        updateViews();
    }

    private void handlePlayerRolesMessage(Bundle bundle) {
        boolean isDealer = bundle.getBoolean(BroadcastKeys.IS_DEALER);
        boolean isSmallBlind = bundle.getBoolean(BroadcastKeys.IS_SMALL_BLIND);
        boolean isBigBlind = bundle.getBoolean(BroadcastKeys.IS_BIG_BLIND);

        // not needed anymore!
    }

    private void handleNewCardMessage(Bundle bundle) {
        Card newHandCard = bundle.getParcelable(BroadcastKeys.CARD);

        // update cards UI
    }

    private void handleWonAmountMessage(Bundle bundle) {
        int wonAmount = bundle.getInt(BroadcastKeys.AMOUNT);

        // not needed
    }

    private void handleUpdateTableMessage(Bundle bundle) {
        this.communityCards = bundle.getParcelableArrayList(BroadcastKeys.CARDS);
        ArrayList<Player> players = bundle.getParcelableArrayList(BroadcastKeys.PLAYERS);
        potSize = bundle.getInt(BroadcastKeys.NEW_POT);

        updateTablePot();
        this.players = players;
        updateViews();
    }

    private void handleYourTurnMessage(Bundle bundle) {
        this.minAmountToRaise = bundle.getInt(BroadcastKeys.MIN_AMOUNT_TO_RAISE);

        Log.e("flow test", "in handleYourTurnMessage()");

        if (minAmountToRaise == 0)
            showPlayerActions(true);
        else
            showPlayerActions(false);
    }

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
    public void drawPlayerCards () {
        int [] playerCards = {R.id.playerCard1, R.id.playerCard2};
        drawCards(playerCards);
    }

    public void drawfirstThreeCommunityCards() {
        int [] firstThreeCommunityCards = {R.id.flop1, R.id.flop2, R.id.flop3};
        drawCards(firstThreeCommunityCards);
    }

    public void drawTurnCard() {
        int[] getTurn={R.id.turn};
        drawCards(getTurn);
    }

    public void drawRiverCard() {
        int[] getRiver ={R.id.river};
        drawCards(getRiver);
    }

    public void drawCards (int [] CardIDs) {
      for(int i : CardIDs) {
            final ImageView cardView = findViewById(i);
       cardView.setImageDrawable(getDrawable(Card.getCards()));
    }
    }


    private void addingPlayerNamesToArray () {
        for(int i=0; i<players.size(); i++) {
            playerNames[i]=players.get(i).getName();
        }
    }

    //If you think somebody was cheating click on the BigRedButton on the Display and choose somebody
    //If you were right - the opposite get´s a penalty, if you were wrong - you get one
    public void showTheCheater () {
        Button btnShowCheater = findViewById(R.id.btn_cheating);
       // CheckBox cheatOn = findViewById(R.id.box_cheatOn);          //should compare with the CheckBox, if it´s clicked

        if(!isCheatingAllowed) {
       //if(!cheatOn.isChecked()){
            btnShowCheater.setEnabled(false);
        } else if (isCheatingAllowed) {
        //} else if (cheatOn.isChecked()) {
            btnShowCheater.setEnabled(true);
            showTheCheater = new ShowTheCheater();
        }

        btnShowCheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addingPlayerNamesToArray();


                    AlertDialog.Builder createDialog = new AlertDialog.Builder(GameActivity.this);

                    createDialog.setTitle("Choose the Cheater! You have 5 seconds");
                    createDialog.setSingleChoiceItems(playerNames, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int indexPosition) {
                            boolean wasCheatingFlag = false;
                            for (int i = 0; i < players.size(); i++) {

                                if (playerNames[indexPosition].equals(players.get(i).getName())) {
                                    showTheCheater.ditHeCheat(players, players.get(0), players.get(indexPosition), players.get(indexPosition).getChipCount() / 5);
                                    // Penalty=1/5 of the ChipCount of the opposite choosen player

                                    updatePlayerChipsViews();
                                /*tvPlayer1Chips = findViewById(R.id.txt_chipsplayer);
                                tvPlayer2Chips = findViewById(R.id.txt_chipsop1);
                                tvPlayer3Chips = findViewById(R.id.text_checkop2);
                                tvPlayer1Chips.setText(String.valueOf(players.get(0).getChipCount()));
                                tvPlayer2Chips.setText(String.valueOf(players.get(1).getChipCount()));
                                tvPlayer3Chips.setText(String.valueOf(players.get(2).getChipCount()));*/

                                    if (players.get(indexPosition).getCheatStatus() == true) {
                                        wasCheatingFlag = true;
                                    }
                                    break;
                                }
                            }
                            dialogInterface.dismiss();
                            if (wasCheatingFlag == true) {  //Shows TOAST whether the player choose right or wrong - dependency to "wasCheating"
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

    //Cheat-Funktion - "DeadMansHand"
    //You can choose any card of the deck and change it with one on your hand
    public void chooseOneCardFromDeck () {

        final Intent deadMansIntent = new Intent(this, CardList_array_adapterActivity.class);


        btnChooseOneCardFromDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(deadMansIntent, 1);
            }
        });

    }


    //Belongs to Cheat-Funktion "DeadMansHand"
    //if you get the result of the "CardList_array_adapterActivity" you choose the card on your hand you want to change then it changes
        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                final String CardId = data.getStringExtra(CardList_array_adapterActivity.resultCardID);


                btnShowTableCard.setVisibility(View.GONE);
                btnProbability.setVisibility(View.GONE);
                btnChooseOneCardFromDeck.setVisibility(View.GONE);

                final int deadMansID = Integer.parseInt(CardId);
                final ImageView playersCardLeft = findViewById(R.id.playerCard1);
                final ImageView playersCardRight = findViewById(R.id.playerCard2);
                Toast.makeText(GameActivity.this, "Click on the Card of your hand you want to change", Toast.LENGTH_SHORT).show();

                playersCardLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        playersCardLeft.setImageDrawable(getDrawable(deadMansID));
                        playersCardRight.setClickable(false);

                    }
                });

                playersCardRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        playersCardRight.setImageDrawable(getDrawable(deadMansID));
                        playersCardLeft.setClickable(false);
                    }
                });
            }
        }





    public void initialiseCheatButtons () {
        btnCheat = findViewById(R.id.btn_cheat);
        btnShowTableCard = findViewById(R.id.btn_eye);
        btnProbability = findViewById(R.id.btn_wahr);
        btnChooseOneCardFromDeck = findViewById(R.id.btn_dead);
    }


    //Hides All ShowTheCheater Buttons if Cheating is not allowed

    public void hideCheatButtons() {
        if(!isCheatingAllowed) {

            btnCheat.setVisibility(View.GONE);
            btnShowTableCard.setVisibility(View.GONE);
            btnProbability.setVisibility(View.GONE);
            btnChooseOneCardFromDeck.setVisibility(View.GONE);
        }
    }

    //Shows The ShowTheCheater Buttons if Cheating is allowed
    public void setCheatButtonsVisible () {
        if(isCheatingAllowed) {

            btnShowTableCard.setVisibility(View.GONE);
            btnProbability.setVisibility(View.GONE);
            btnChooseOneCardFromDeck.setVisibility(View.GONE);

            btnCheat.setVisibility(View.VISIBLE);
            btnCheat.setOnClickListener(new View.OnClickListener() {

                // onClick on the "ShowTheCheater"-Button the showTheCheater-option can be chose
                @Override
                public void onClick(View view) {

                    btnShowTableCard.setVisibility(View.VISIBLE);
                    btnProbability.setVisibility(View.VISIBLE);
                    btnChooseOneCardFromDeck.setVisibility(View.VISIBLE);
                }
            });

        }
    }

}


