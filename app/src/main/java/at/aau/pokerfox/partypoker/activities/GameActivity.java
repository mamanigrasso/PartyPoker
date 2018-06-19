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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.R;
import at.aau.pokerfox.partypoker.model.CardDeck;
import at.aau.pokerfox.partypoker.model.ProbCheating;
import at.aau.pokerfox.partypoker.model.ShowTheCheater;
import at.aau.pokerfox.partypoker.model.Game;
import at.aau.pokerfox.partypoker.model.ModActInterface;
import at.aau.pokerfox.partypoker.model.Player;
import at.aau.pokerfox.partypoker.model.Card;
import at.aau.pokerfox.partypoker.model.ShowWinnerTask;
import at.aau.pokerfox.partypoker.model.network.BroadcastKeys;
import at.aau.pokerfox.partypoker.model.network.Broadcasts;
import at.aau.pokerfox.partypoker.model.network.messages.client.ActionMessage;
import at.aau.pokerfox.partypoker.model.network.messages.client.CheatPenaltyMessage;
import at.aau.pokerfox.partypoker.model.network.messages.client.ReplaceCardMessage;

import static at.aau.pokerfox.partypoker.model.network.Broadcasts.ACTION_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.CHEAT_PENALTY_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.INIT_GAME_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.NEW_CARD_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.REPLACE_CARD_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.SHOW_WINNER_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.UPDATE_TABLE_MESSAGE;
import static at.aau.pokerfox.partypoker.model.network.Broadcasts.YOUR_TURN_MESSAGE;

/**
 * Created by TimoS on 04.04.2018.
 */

public class GameActivity extends AppCompatActivity implements ModActInterface {

    private final int MAX_PLAYER_COUNT = 6;
    private String[] playerNames;
    //= {"Marco", "Mathias","Timo","Michael","Manuel","Andreas"};

    private boolean isCheatingAllowed = true;
    private ShowTheCheater showTheCheater;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Card> communityCards = new ArrayList<>();
    private PokerBroadcastReceiver receiver;
    private int bigBlind;
    private int playerPot;
    private int minAmountToRaise;
    private int potSize = 0;
    private String myDeviceName = null;
    private String myPlayerName = null;
    private boolean eyePossible = false;
    private boolean cheatModeEyeOn = false;
    private boolean performTurn = false;
    private int lastCardCnt = 0;
    private boolean raiseActive = false;
    private int raiseAmount = 0;
    private int sbMinAmount = 0;
    private boolean initGameMessageReceived = false;
    private boolean cheatOptionsVisible = false;

    private TextView tvTablePot;

    private TextView tvPlayer1Name;
    private TextView tvPlayer2Name;
    private TextView tvPlayer3Name;
    private TextView tvPlayer4Name;
    private TextView tvPlayer5Name;
    private TextView tvPlayer6Name;
    private ArrayList<TextView> tvPlayerNames;

    private TextView tvPlayer1Chips;
    private TextView tvPlayer2Chips;
    private TextView tvPlayer3Chips;
    private TextView tvPlayer4Chips;
    private TextView tvPlayer5Chips;
    private TextView tvPlayer6Chips;
    private ArrayList<TextView> tvPlayerChips;

    private TextView tvPlayer1Bid;
    private TextView tvPlayer2Bid;
    private TextView tvPlayer3Bid;
    private TextView tvPlayer4Bid;
    private TextView tvPlayer5Bid;
    private TextView tvPlayer6Bid;
    private ArrayList<TextView> tvPlayerBids;

    private ImageView ivPlayer1BigBlind;
    private ImageView ivPlayer2BigBlind;
    private ImageView ivPlayer3BigBlind;
    private ImageView ivPlayer4BigBlind;
    private ImageView ivPlayer5BigBlind;
    private ImageView ivPlayer6BigBlind;
    private ArrayList<ImageView> ivPlayerBigBlinds;

    private ImageView ivPlayer1SmallBlind;
    private ImageView ivPlayer2SmallBlind;
    private ImageView ivPlayer3SmallBlind;
    private ImageView ivPlayer4SmallBlind;
    private ImageView ivPlayer5SmallBlind;
    private ImageView ivPlayer6SmallBlind;
    private ArrayList<ImageView> ivPlayerSmallBlinds;

    private ImageView ivPlayer1Dealer;
    private ImageView ivPlayer2Dealer;
    private ImageView ivPlayer3Dealer;
    private ImageView ivPlayer4Dealer;
    private ImageView ivPlayer5Dealer;
    private ImageView ivPlayer6Dealer;
    private ArrayList<ImageView> ivPlayerDealers;

    private TextView tvPlayer1Status;
    private TextView tvPlayer2Status;
    private TextView tvPlayer3Status;
    private TextView tvPlayer4Status;
    private TextView tvPlayer5Status;
    private TextView tvPlayer6Status;
    private ArrayList<TextView> tvPlayerStates;

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
    private ArrayList<ImageView> ivPlayerCards1;
    private ArrayList<ImageView> ivPlayerCards2;

    private SeekBar sbRaiseAmount = null;

    private ImageView ivTableCard1;
    private ImageView ivTableCard2;
    private ImageView ivTableCard3;
    private ImageView ivTableCard4;
    private ImageView ivTableCard5;
    private ArrayList<ImageView> ivTableCards;

    private Button buttonCheck;
    private Button buttonFold;
    private Button buttonRaise;
    private Button btnCheat;
    private Button btnShowTableCard;
    private Button btnProbability;
    private Button btnChooseOneCardFromDeck;
    private Button btnCheatingAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_table);

        myDeviceName = getIntent().getExtras().getString(MainActivity.BUNDLE_DEVICE_NAME);
        myPlayerName = getIntent().getExtras().getString(MainActivity.BUNDLE_PLAYER_NAME);

        if (PartyPokerApplication.isHost()) {
            Bundle bundle = getIntent().getExtras();
            bigBlind = bundle.getInt(HostGameActivity.BUNDLE_BIG_BLIND);
            playerPot = bundle.getInt(HostGameActivity.BUNDLE_PLAYER_POT);
            isCheatingAllowed = bundle.getBoolean(HostGameActivity.BUNDLE_CHEATING_ALLOWED);

            prepareGame();
            initGame();
        }
        this.receiver = new PokerBroadcastReceiver();

        if (PartyPokerApplication.isHost())
            Game.getInstance().startRound();
    }

    private void prepareGame() {
        players = new ArrayList<>();

        Player myself = new Player(myPlayerName);
        myself.setIsHost(true);
        myself.setDevice(PartyPokerApplication.getNetwork().thisDevice);
        players.add(myself);

        Game.init(bigBlind, 15, playerPot, PartyPokerApplication.getConnectedDevices().size()+1, isCheatingAllowed, this);

        Game.addPlayer(myself);

        for (SalutDevice device : PartyPokerApplication.getConnectedDevices()) {
            Player p = new Player(device.readableName);
            p.setDevice(device);
            players.add(p);
            Game.addPlayer(p);
        }

        Game.getInstance().sendInitGameMessage();

        try {
            Thread.sleep(500); // give init game message some time to be transferred
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initGame() {
        createAllViews();
        hideAllUnusedViews();
        setPlayerNames();
        updateViews();
        initSeekBar();
        initCheating();
        initPlayerButtons();
        hidePlayerActions();
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

        tvPlayerNames = new ArrayList<TextView>();
        tvPlayerNames.add(tvPlayer1Name);
        tvPlayerNames.add(tvPlayer2Name);
        tvPlayerNames.add(tvPlayer3Name);
        tvPlayerNames.add(tvPlayer4Name);
        tvPlayerNames.add(tvPlayer5Name);
        tvPlayerNames.add(tvPlayer6Name);
    }

    private void setPlayerNames() {
        int i=1;
        for (Player p : players) {
            if (p.getDeviceId().equals(myDeviceName))    // if p is this player
                tvPlayerNames.get(0).setText(p.getName());
            else
                tvPlayerNames.get(i++).setText(p.getName());
        }
    }

    private void createPlayerChipsViews() {
        tvPlayer1Chips = findViewById(R.id.txt_chipsplayer);
        tvPlayer2Chips = findViewById(R.id.txt_chipsop1);
        tvPlayer3Chips = findViewById(R.id.txt_chipsop2);
        tvPlayer4Chips = findViewById(R.id.txt_chipsop3);
        tvPlayer5Chips = findViewById(R.id.txt_chipsop4);
        tvPlayer6Chips = findViewById(R.id.txt_chipsop5);

        tvPlayerChips = new ArrayList<TextView>();
        tvPlayerChips.add(tvPlayer1Chips);
        tvPlayerChips.add(tvPlayer2Chips);
        tvPlayerChips.add(tvPlayer3Chips);
        tvPlayerChips.add(tvPlayer4Chips);
        tvPlayerChips.add(tvPlayer5Chips);
        tvPlayerChips.add(tvPlayer6Chips);
    }

    private void updatePlayerChipsViews() {
        int i=1;
        for (Player p : players) {
            if (p.getDeviceId().equals(myDeviceName))    // if p is this player
                tvPlayerChips.get(0).setText(p.getChipCount() + "");
            else
                tvPlayerChips.get(i++).setText(p.getChipCount() + "");
        }
    }

    private void createPlayerBidViews() {
        tvPlayer1Bid = findViewById(R.id.gebotself);
        tvPlayer2Bid = findViewById(R.id.gebotop1);
        tvPlayer3Bid = findViewById(R.id.gebotop2);
        tvPlayer4Bid = findViewById(R.id.gebotop3);
        tvPlayer5Bid = findViewById(R.id.gebotop4);
        tvPlayer6Bid = findViewById(R.id.gebotop5);

        tvPlayerBids = new ArrayList<TextView>();
        tvPlayerBids.add(tvPlayer1Bid);
        tvPlayerBids.add(tvPlayer2Bid);
        tvPlayerBids.add(tvPlayer3Bid);
        tvPlayerBids.add(tvPlayer4Bid);
        tvPlayerBids.add(tvPlayer5Bid);
        tvPlayerBids.add(tvPlayer6Bid);
    }

    private void updatePlayerBidViews() {
        int i=1;
        for (Player p : players) {
            if (p.getDeviceId().equals(myDeviceName))    // if p is this player
                tvPlayerBids.get(0).setText(p.getCurrentBid() + "");
            else
                tvPlayerBids.get(i++).setText(p.getCurrentBid() + "");
        }
    }

    private void createPlayerRoleViews() {
        ivPlayer1BigBlind = findViewById(R.id.img_bigblindopself);
        ivPlayer2BigBlind = findViewById(R.id.img_bigblindop1);
        ivPlayer3BigBlind = findViewById(R.id.img_bigblindop2);
        ivPlayer4BigBlind = findViewById(R.id.img_bigblindop3);
        ivPlayer5BigBlind = findViewById(R.id.img_bigblindop4);
        ivPlayer6BigBlind = findViewById(R.id.img_bigblindop5);

        ivPlayerBigBlinds = new ArrayList<ImageView>();
        ivPlayerBigBlinds.add(ivPlayer1BigBlind);
        ivPlayerBigBlinds.add(ivPlayer2BigBlind);
        ivPlayerBigBlinds.add(ivPlayer3BigBlind);
        ivPlayerBigBlinds.add(ivPlayer4BigBlind);
        ivPlayerBigBlinds.add(ivPlayer5BigBlind);
        ivPlayerBigBlinds.add(ivPlayer6BigBlind);

        ivPlayer1SmallBlind = findViewById(R.id.img_smallblindself);
        ivPlayer2SmallBlind = findViewById(R.id.img_smallblindop1);
        ivPlayer3SmallBlind = findViewById(R.id.img_smallblindop2);
        ivPlayer4SmallBlind = findViewById(R.id.img_smallblindop3);
        ivPlayer5SmallBlind = findViewById(R.id.img_smallblindop4);
        ivPlayer6SmallBlind = findViewById(R.id.img_smallblindop5);

        ivPlayerSmallBlinds = new ArrayList<ImageView>();
        ivPlayerSmallBlinds.add(ivPlayer1SmallBlind);
        ivPlayerSmallBlinds.add(ivPlayer2SmallBlind);
        ivPlayerSmallBlinds.add(ivPlayer3SmallBlind);
        ivPlayerSmallBlinds.add(ivPlayer4SmallBlind);
        ivPlayerSmallBlinds.add(ivPlayer5SmallBlind);
        ivPlayerSmallBlinds.add(ivPlayer6SmallBlind);

        ivPlayer1Dealer = findViewById(R.id.img_dealerbtnself);
        ivPlayer2Dealer = findViewById(R.id.img_dealerbtnop1);
        ivPlayer3Dealer = findViewById(R.id.img_dealerbtnop2);
        ivPlayer4Dealer = findViewById(R.id.img_dealerbtnop3);
        ivPlayer5Dealer = findViewById(R.id.img_dealerbtnop4);
        ivPlayer6Dealer = findViewById(R.id.img_dealerbtnop5);

        ivPlayerDealers = new ArrayList<ImageView>();
        ivPlayerDealers.add(ivPlayer1Dealer);
        ivPlayerDealers.add(ivPlayer2Dealer);
        ivPlayerDealers.add(ivPlayer3Dealer);
        ivPlayerDealers.add(ivPlayer4Dealer);
        ivPlayerDealers.add(ivPlayer5Dealer);
        ivPlayerDealers.add(ivPlayer6Dealer);
    }

    private void updatePlayerRoleViews() {
        int i=1;
        for (Player p : players) {
            if (p.getDeviceId().equals(myDeviceName)) {   // if p is this player
                ivPlayerBigBlinds.get(0).setVisibility(p.isBigBlind() ? View.VISIBLE : View.INVISIBLE);
                ivPlayerSmallBlinds.get(0).setVisibility(p.isSmallBlind() ? View.VISIBLE : View.INVISIBLE);
                ivPlayerDealers.get(0).setVisibility(p.isDealer() ? View.VISIBLE : View.INVISIBLE);
            }
            else {
                ivPlayerBigBlinds.get(i).setVisibility(p.isBigBlind() ? View.VISIBLE : View.INVISIBLE);
                ivPlayerSmallBlinds.get(i).setVisibility(p.isSmallBlind() ? View.VISIBLE : View.INVISIBLE);
                ivPlayerDealers.get(i).setVisibility(p.isDealer() ? View.VISIBLE : View.INVISIBLE);
                i++;
            }
        }
    }

    private void createPlayerStatusViews() {
        tvPlayer1Status = findViewById(R.id.text_checkself);
        tvPlayer2Status = findViewById(R.id.text_checkop1);
        tvPlayer3Status = findViewById(R.id.text_checkop2);
        tvPlayer4Status = findViewById(R.id.text_checkop3);
        tvPlayer5Status = findViewById(R.id.text_checkop4);
        tvPlayer6Status = findViewById(R.id.text_checkop5);

        tvPlayerStates = new ArrayList<TextView>();
        tvPlayerStates.add(tvPlayer1Status);
        tvPlayerStates.add(tvPlayer2Status);
        tvPlayerStates.add(tvPlayer3Status);
        tvPlayerStates.add(tvPlayer4Status);
        tvPlayerStates.add(tvPlayer5Status);
        tvPlayerStates.add(tvPlayer6Status);
    }

    private void updatePlayerStatusViews() {
        int i=1;
        for (Player p : players) {
            if (p.getDeviceId().equals(myDeviceName))    // if p is this player
                tvPlayerStates.get(0).setText(p.getStatus());
            else
                tvPlayerStates.get(i++).setText(p.getStatus());
        }
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

        ivPlayerCards1 = new ArrayList<ImageView>();
        ivPlayerCards1.add(ivPlayer1Card1);
        ivPlayerCards1.add(ivPlayer2Card1);
        ivPlayerCards1.add(ivPlayer3Card1);
        ivPlayerCards1.add(ivPlayer4Card1);
        ivPlayerCards1.add(ivPlayer5Card1);
        ivPlayerCards1.add(ivPlayer6Card1);

        ivPlayerCards2 = new ArrayList<ImageView>();
        ivPlayerCards2.add(ivPlayer1Card2);
        ivPlayerCards2.add(ivPlayer2Card2);
        ivPlayerCards2.add(ivPlayer3Card2);
        ivPlayerCards2.add(ivPlayer4Card2);
        ivPlayerCards2.add(ivPlayer5Card2);
        ivPlayerCards2.add(ivPlayer6Card2);


        for (ImageView actView : ivPlayerCards1) {
            View.OnClickListener clickListener1 = new View.OnClickListener() {
                public void onClick(View v) {
                    if (cheatModeEyeOn) {
                        // CHECK IF CARD IS ACTIVE
                        turnCard(v.getId(), v.getId());
                        cheatModeEyeOn = false;
                    }
                }
            };
            actView.setOnClickListener(clickListener1);

        }

        for (ImageView actView : ivPlayerCards2) {
            View.OnClickListener clickListener2 = new View.OnClickListener() {
                public void onClick(View v) {
                    if (cheatModeEyeOn) {
                        // CHECK IF CARD ACTIVE
                        turnCard(v.getId(), v.getId());
                        cheatModeEyeOn = false;
                    }
                }
            };
            actView.setOnClickListener(clickListener2);

        }
    }

    private void updatePlayerCardViews(boolean showAllOtherPlayerCards) {
        int i=1;
        for (Player p : players) {
            if (p.getDeviceId().equals(myDeviceName)) {   // if p is this player
                if (p.getCard1() == null)
                    ivPlayerCards1.get(0).setVisibility(View.INVISIBLE);
                else {
                    ivPlayerCards1.get(0).setVisibility(View.VISIBLE);
                    ivPlayerCards1.get(0).setImageDrawable(getDrawable(p.getCard1().getDrawableID()));
                }

                if (p.getCard2() == null)
                    ivPlayerCards2.get(0).setVisibility(View.INVISIBLE);
                else {
                    ivPlayerCards2.get(0).setVisibility(View.VISIBLE);
                    ivPlayerCards2.get(0).setImageDrawable(getDrawable(p.getCard2().getDrawableID()));
                }
            }
            else {
                if (p.getCard1() == null)
                    ivPlayerCards1.get(i).setVisibility(View.INVISIBLE);
                else {
                    ivPlayerCards1.get(i).setVisibility(View.VISIBLE);

                    if (showAllOtherPlayerCards) {
                        ivPlayerCards1.get(i).setImageDrawable(getDrawable(p.getCard1().getDrawableID()));
                    }
                    else {
                        ivPlayerCards1.get(i).setImageDrawable(getDrawable(R.drawable.card_back));
                    }
                }

                if (p.getCard2() == null)
                    ivPlayerCards2.get(i).setVisibility(View.INVISIBLE);
                else {
                    ivPlayerCards2.get(i).setVisibility(View.VISIBLE);

                    if (showAllOtherPlayerCards) {
                        ivPlayerCards2.get(i).setImageDrawable(getDrawable(p.getCard2().getDrawableID()));
                    }
                    else {
                        ivPlayerCards2.get(i).setImageDrawable(getDrawable(R.drawable.card_back));
                    }
                }
                i++;
            }
        }
    }

    private void createTableCardViews() {
        ivTableCard1 = findViewById(R.id.flop1);
        ivTableCard2 = findViewById(R.id.flop2);
        ivTableCard3 = findViewById(R.id.flop3);
        ivTableCard4 = findViewById(R.id.turn);
        ivTableCard5 = findViewById(R.id.river);

        ivTableCards = new ArrayList<ImageView>();
        ivTableCards.add(ivTableCard1);
        ivTableCards.add(ivTableCard2);
        ivTableCards.add(ivTableCard3);
        ivTableCards.add(ivTableCard4);
        ivTableCards.add(ivTableCard5);
    }

    private void updateTableCardViews() {
        int i=0;

        for (ImageView ivTableCard : ivTableCards) {
            ivTableCard.setVisibility(View.INVISIBLE);
        }

        for (Card c : communityCards) {
            ivTableCards.get(i).setVisibility(View.VISIBLE);
            ivTableCards.get(i).setImageDrawable(getDrawable(c.getDrawableID()));
            i++;
        }
        if (performTurn)
            turnForXPlayers(false, false, false, false, false, i==3, i==3, i==3, i==4, i==5);

    }

    private void createAllViews() {
        createTablePotView();
        createPlayerNameViews();
        createPlayerChipsViews();
        createPlayerBidViews();
        createPlayerRoleViews();
        createPlayerStatusViews();
        createPlayerCardViews();
        createTableCardViews();
    }

    private void hideAllUnusedViews() {
        for (int i=0; i < players.size(); i++) {
            tvPlayerNames.get(i).setVisibility(View.VISIBLE);
            tvPlayerChips.get(i).setVisibility(View.VISIBLE);
            tvPlayerBids.get(i).setVisibility(View.VISIBLE);
            tvPlayerStates.get(i).setVisibility(View.VISIBLE);
            ivPlayerCards1.get(i).setVisibility(View.VISIBLE);
            ivPlayerCards2.get(i).setVisibility(View.VISIBLE);
        }

        for (int i=players.size(); i < MAX_PLAYER_COUNT; i++) {
            tvPlayerNames.get(i).setVisibility(View.INVISIBLE);
            tvPlayerChips.get(i).setVisibility(View.INVISIBLE);
            tvPlayerBids.get(i).setVisibility(View.INVISIBLE);
            tvPlayerStates.get(i).setVisibility(View.INVISIBLE);
            ivPlayerCards1.get(i).setVisibility(View.INVISIBLE);
            ivPlayerCards2.get(i).setVisibility(View.INVISIBLE);
        }
    }

    private void updateViews() {
        updateTablePot();
        updatePlayerChipsViews();
        updatePlayerBidViews();
        updatePlayerRoleViews();
        updatePlayerStatusViews();
        updatePlayerCardViews(false);
        updateTableCardViews();
    }

    private void initPlayerButtons() {
        buttonCheck = (Button)findViewById(R.id.btn_check);
        buttonFold = (Button)findViewById(R.id.btn_fold);
        buttonRaise = (Button)findViewById(R.id.btn_raise);
    }
    @Override
    public void hidePlayerActions() {
        buttonCheck.setVisibility(View.INVISIBLE);
        buttonFold.setVisibility(View.INVISIBLE);
        buttonRaise.setVisibility(View.INVISIBLE);
        btnCheat.setVisibility(View.INVISIBLE);

        if (sbRaiseAmount != null)
            sbRaiseAmount.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showPlayerActions(int minAmountToRaise) {
        buttonFold.setVisibility(View.VISIBLE);
        buttonRaise.setVisibility(View.VISIBLE);
        buttonCheck.setVisibility(View.VISIBLE);

        this.minAmountToRaise = minAmountToRaise;

        if (minAmountToRaise == 0)
            buttonCheck.setText("CHECK");
        else
            buttonCheck.setText("CALL(" + minAmountToRaise + ")");

        if (isCheatingAllowed) {
            btnCheat.setVisibility(View.VISIBLE);
        } else {
            btnCheat.setVisibility(View.INVISIBLE);
        }
    }

    private void prepareAndSendActionMessage(int amount, boolean hasFolded) {
        ActionMessage message = new ActionMessage();
        message.Amount = amount;
        message.HasFolded = hasFolded;
        PartyPokerApplication.getMessageHandler().sendMessageToHost(message);
    }

    public void buttonCheckPressed(View v) {
        hidePlayerActions();

        if (PartyPokerApplication.isHost()) {
            Game.getInstance().playerBid(this.minAmountToRaise);
        }
        else {
            prepareAndSendActionMessage(this.minAmountToRaise, false);
        }
    }

    @Override
    public void update(ArrayList<Card> cards, int pot, ArrayList<Player> players) {
        this.communityCards = cards;
        this.potSize = pot;
        this.players = players;

        updateViews();
    }

    @Override
    public void showWinner(String winnerInfo, boolean finalWinner) {
        updatePlayerCardViews(true);

        AlertDialog.Builder winnerDialog = new AlertDialog.Builder(GameActivity.this);

        if (finalWinner)
            winnerDialog.setTitle("Game over!");
        else
            winnerDialog.setTitle("And the winner is...");

        winnerDialog.setMessage(winnerInfo);

        final AlertDialog winnerAlert = winnerDialog.create();
        winnerAlert.show();

        if (!finalWinner) {
            final Timer timeoutDialog = new Timer();

            timeoutDialog.schedule(new TimerTask() {

                public void run() {
                    winnerAlert.dismiss();
                    timeoutDialog.cancel();
                }
            }, 5000);

            if (PartyPokerApplication.isHost()) {
                ShowWinnerTask showWinnerTask = new ShowWinnerTask();
                showWinnerTask.execute();
            }
        }
    }

    public void buttonFoldPressed(View v) {
        hidePlayerActions();

        if (PartyPokerApplication.isHost()) {
            Game.getInstance().playerFolded();
        }
        else {
            prepareAndSendActionMessage(0, true);
        }
    }

    public void buttonEyePressed(View v) {
        //prepareAndSendActionMessage(0, true, false);
        if (eyePossible) {
            cheatModeEyeOn = true;
            //turnForXPlayers(false, false, false, false, false, false, false, false, false, true);
        }
    }

    public void buttonRaisePressed(View v) {
        if (!raiseActive) {
            int playerChips = 0;
            int playerBid = 0;
            for (Player p : players) {
                if (p.getDeviceId().equals(myDeviceName)) {
                    playerChips = p.getChipCount();
                    playerBid = p.getCurrentBid();
                }
            }

            buttonCheck.setVisibility(View.INVISIBLE);
            buttonFold.setVisibility(View.INVISIBLE);

            if (minAmountToRaise == 0) {
                this.sbMinAmount = bigBlind;
            }
            else {
                this.sbMinAmount = minAmountToRaise;
            }

            sbRaiseAmount.setMax((playerChips - sbMinAmount) / this.bigBlind);  // (max - min) / step

            sbRaiseAmount.setVisibility(View.VISIBLE);
            raiseActive = true;
        }
        else {
            raiseActive = false;
            hidePlayerActions();
            if (PartyPokerApplication.isHost()) {
                Game.getInstance().playerBid(raiseAmount);
            }
            else {
                prepareAndSendActionMessage(raiseAmount, false);
            }
        }
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
            int[] drawableIds = {
                    players.get(0).getCard1().getDrawableID(),
                    players.get(0).getCard2().getDrawableID()
            };
            turnCards(myIds, drawableIds);
        }
        if (player1) {
            int[] myIds = {R.id.opponent1Card1, R.id.opponent1Card2};
            int[] drawableIds = {
                    players.get(1).getCard1().getDrawableID(),
                    players.get(1).getCard2().getDrawableID()
            };
            turnCards(myIds, drawableIds);
        }
        if (player2) {
            int[] myIds = {R.id.opponent2Card1, R.id.opponent2Card2};
            int[] drawableIds = {
                    players.get(2).getCard1().getDrawableID(),
                    players.get(2).getCard2().getDrawableID()
            };
            turnCards(myIds, drawableIds);
        }
        if (player3) {
            int[] myIds = {R.id.opponent3Card1, R.id.opponent3Card2};
            int[] drawableIds = {
                    players.get(3).getCard1().getDrawableID(),
                    players.get(3).getCard2().getDrawableID()
            };
            turnCards(myIds, drawableIds);
        }
        if (player4) {
            int[] myIds = {R.id.opponent4Card1, R.id.opponent4Card2};
            int[] drawableIds = {
                    players.get(4).getCard1().getDrawableID(),
                    players.get(4).getCard2().getDrawableID()
            };
            turnCards(myIds, drawableIds);
        }
        if (player5) {
            int[] myIds = {R.id.opponent5Card1, R.id.opponent5Card2};
            int[] drawableIds = {
                    players.get(5).getCard1().getDrawableID(),
                    players.get(5).getCard2().getDrawableID()
            };
            turnCards(myIds, drawableIds);
        }
        if (flop1) {
            int[] myIds = {R.id.flop1};
            int[] drawableIds = {this.communityCards.get(0).getDrawableID()};
            turnCards(myIds, drawableIds);
        }
        if (flop2) {
            int[] myIds = {R.id.flop2};
            int[] drawableIds = {this.communityCards.get(1).getDrawableID()};
            turnCards(myIds, drawableIds);

        }
        if (flop3) {
            int[] myIds = {R.id.flop3};
            int[] drawableIds = {this.communityCards.get(2).getDrawableID()};
            turnCards(myIds, drawableIds);
        }
        if (turn) {
            int[] myIds = {R.id.turn};
            int[] drawableIds = {this.communityCards.get(3).getDrawableID()};
            turnCards(myIds, drawableIds);
        }
        if (river) {
            int[] myIds = {R.id.river};
            int[] drawableIds = {this.communityCards.get(4).getDrawableID()};
            turnCards(myIds, drawableIds);
        }

        // .. beliebig erweitern
    }
    public void turnMiddleCards() {
        int[] myIds = {R.id.flop1, R.id.flop2, R.id.flop3, R.id.turn, R.id.river};
        int[] drawableIds = {
                this.communityCards.get(0).getDrawableID(),
                this.communityCards.get(1).getDrawableID(),
                this.communityCards.get(2).getDrawableID(),
                this.communityCards.get(3).getDrawableID(),
                this.communityCards.get(4).getDrawableID()
        };
        turnCards(myIds, drawableIds);
    }

    public void turnOwnCards() {
        int[] myIds = {R.id.playerCard1, R.id.playerCard2};
        int[] drawableIds = {
                players.get(0).getCard1().getDrawableID(),
                players.get(0).getCard2().getDrawableID()
        };
        turnCards(myIds, drawableIds);
    }

    public void turnCard(int viewId, int cardId) {
        int[] myIds = {viewId};
        int[] drawableIds = {cardId};
        turnCards(myIds, drawableIds);
    }

    public void turnCards(int[] viewIds, int[] cards) {
        if (viewIds == null || cards == null || viewIds.length != cards.length)
            return;
        int drawableCnt = 0;
        for (int viewId : viewIds) {
            final ImageView myView = findViewById(viewId);
            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(myView, "scaleX", 1f, 0f);
            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(myView, "scaleX", 0f, 1f);
            final int actDrawableID = cards[drawableCnt];
            drawableCnt ++;
            oa1.setInterpolator(new DecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
            oa1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setImageResource(actDrawableID);

                    oa2.start();
                }
            });
            oa1.start();
        }
    }

    private void registerForPokerBroadcasts(@NonNull PokerBroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter(ACTION_MESSAGE);
        filter.addAction(Broadcasts.REPLACE_CARD_MESSAGE);
        filter.addAction(Broadcasts.CHEAT_PENALTY_MESSAGE);
        filter.addAction(Broadcasts.INIT_GAME_MESSAGE);
        filter.addAction(Broadcasts.UPDATE_TABLE_MESSAGE);
        filter.addAction(Broadcasts.YOUR_TURN_MESSAGE);
        filter.addAction(Broadcasts.NEW_CARD_MESSAGE);
        filter.addAction(Broadcasts.SHOW_WINNER_MESSAGE);
        registerReceiver(receiver, filter);
    }

    private void handleActionMessage(Bundle bundle) {
        int amount = bundle.getInt(BroadcastKeys.AMOUNT);
        boolean hasFolded = bundle.getBoolean(BroadcastKeys.HAS_FOLDED);

        if (hasFolded) {
            Game.getInstance().playerFolded();
        } else {
            Game.getInstance().playerBid(amount);
        }
    }

    private void handleReplaceCardMessage(Bundle bundle) {
        Card replacementCard = bundle.getParcelable(BroadcastKeys.REPLACEMENT_CARD);
        boolean cardToReplace = bundle.getBoolean(BroadcastKeys.CARD_TO_REPLACE);

        Game.getInstance().replacePlayersCard(cardToReplace, replacementCard);
    }

    private void handleCheatPenaltyMessage(Bundle bundle) {
        String complainer = bundle.getString(BroadcastKeys.COMPLAINER);
        String cheater = bundle.getString(BroadcastKeys.CHEATER);
        boolean penalizeCheater = bundle.getBoolean(BroadcastKeys.PENALIZECHEATER);

        Game.getInstance().cheatPenalty(complainer, cheater, penalizeCheater);
    }

    private void handleInitGameMessage(Bundle bundle) {
        ArrayList<Player> players = bundle.getParcelableArrayList(BroadcastKeys.PLAYERS);
        isCheatingAllowed = bundle.getBoolean(BroadcastKeys.CHEAT_ON);
        int bigBlind = bundle.getInt(BroadcastKeys.BIG_BLIND);

        this.players = players;
        this.bigBlind = bigBlind;

        initGameMessageReceived = true;
        initGame();
    }

    private void handleNewCardMessage(Bundle bundle) {
        Card newHandCard = bundle.getParcelable(BroadcastKeys.CARD);

        // update cards UI
    }

    private void handleUpdateTableMessage(Bundle bundle) {
        this.communityCards = bundle.getParcelableArrayList(BroadcastKeys.CARDS);
        ArrayList<Player> players = bundle.getParcelableArrayList(BroadcastKeys.PLAYERS);
        potSize = bundle.getInt(BroadcastKeys.NEW_POT);
        if (lastCardCnt < this.communityCards.size()) {
            lastCardCnt = this.communityCards.size();
            performTurn = true;
        } else {
            performTurn = false;
        }
        if (this.communityCards != null && this.communityCards.size() >= 4)
            eyePossible = true;

        if (!initGameMessageReceived)
            throw new RuntimeException("Communication Error! InitGameMessage not received!");

        this.players = players;

        updateViews();
    }

    private void handleShowWinnerMessage(Bundle bundle) {
        String winnerInfo = bundle.getString(BroadcastKeys.WINNER_INFO);
        boolean finalWinner = bundle.getBoolean(BroadcastKeys.FINAL_WINNER);

        showWinner(winnerInfo, finalWinner);
    }

    private void handleYourTurnMessage(Bundle bundle) {
        this.minAmountToRaise = bundle.getInt(BroadcastKeys.MIN_AMOUNT_TO_RAISE);

        showPlayerActions(minAmountToRaise);
    }

    private class PokerBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();

            System.out.println("Got message: " + action);

            switch (action) {
                case ACTION_MESSAGE:
                    handleActionMessage(extras);
                    break;
                case REPLACE_CARD_MESSAGE:
                    handleReplaceCardMessage(extras);
                    break;
                case CHEAT_PENALTY_MESSAGE:
                    handleCheatPenaltyMessage(extras);
                    break;
                case INIT_GAME_MESSAGE:
                    handleInitGameMessage(extras);
                    break;
                case NEW_CARD_MESSAGE:
                    handleNewCardMessage(extras);
                    break;
                case UPDATE_TABLE_MESSAGE:
                    handleUpdateTableMessage(extras);
                    break;
                case SHOW_WINNER_MESSAGE:
                    handleShowWinnerMessage(extras);
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
        playerNames = new String[players.size()-1];
        int j=0;

        for(int i=0; i<players.size(); i++) {
            if (!players.get(i).getName().equals(myPlayerName))
                playerNames[j++]=players.get(i).getName();
        }
    }

    //If you think somebody was cheating click on the BigRedButton on the Display and choose somebody
    //If you were right - the opposite get´s a penalty, if you were wrong - you get one
    public void showTheCheater () {
        if (isCheatingAllowed) {
            showTheCheater = new ShowTheCheater();
        }

        btnCheatingAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addingPlayerNamesToArray();

                AlertDialog.Builder createDialog = new AlertDialog.Builder(GameActivity.this);

                createDialog.setTitle("Choose the Cheater! You have 5 seconds");
                createDialog.setSingleChoiceItems(playerNames, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int indexPosition) {
                        Player cheater = getPlayerByName(playerNames[indexPosition]);

                        if (PartyPokerApplication.isHost()) {
                            Game.getInstance().cheatPenalty(myPlayerName, playerNames[indexPosition], cheater.getCheatStatus());
                        } else {
                            CheatPenaltyMessage message = new CheatPenaltyMessage();
                            message.complainer = myPlayerName;
                            message.cheater = playerNames[indexPosition];
                            message.penalizeCheater = cheater.getCheatStatus();
                            PartyPokerApplication.getMessageHandler().sendMessageToHost(message);
                        }

                        updatePlayerChipsViews();

                        dialogInterface.dismiss();
                        if (cheater.getCheatStatus()) {  //Shows TOAST whether the player choose right or wrong - dependency to "wasCheating"
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

    private Player getPlayerByName(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName))
                return p;
        }

        return null;
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

    public void initSeekBar() {
        sbRaiseAmount = findViewById(R.id.sbRaiseAmount);
        final int bigBlind = this.bigBlind;

        sbRaiseAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(GameActivity.this, String.valueOf(raiseAmount),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                raiseAmount = sbMinAmount + (bigBlind*progress);
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

                    if (PartyPokerApplication.isHost())
                        Game.getInstance().replacePlayersCard(true, CardDeck.getCardFromDrawableCardId(deadMansID));
                    else {
                        ReplaceCardMessage message = new ReplaceCardMessage();
                        message.replaceCard1 = true;
                        message.replacementCard = CardDeck.getCardFromDrawableCardId(deadMansID);
                        PartyPokerApplication.getMessageHandler().sendMessageToHost(message);
                    }

                    playersCardLeft.setImageDrawable(getDrawable(deadMansID));
                    playersCardRight.setClickable(false);

                }
            });

            playersCardRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (PartyPokerApplication.isHost())
                        Game.getInstance().replacePlayersCard(false, CardDeck.getCardFromDrawableCardId(deadMansID));
                    else {
                        ReplaceCardMessage message = new ReplaceCardMessage();
                        message.replaceCard1 = false;
                        message.replacementCard = CardDeck.getCardFromDrawableCardId(deadMansID);
                        PartyPokerApplication.getMessageHandler().sendMessageToHost(message);
                    }

                    playersCardRight.setImageDrawable(getDrawable(deadMansID));
                    playersCardLeft.setClickable(false);
                }
            });
        }
    }

    public void initCheating() {
        initialiseCheatButtons();
        hideCheatButtons();
        setCheatButtonsVisible();
        showTheCheater();
        chooseOneCardFromDeck();
        testProbability();
    }

    public void initialiseCheatButtons() {
        btnCheat = findViewById(R.id.btn_cheat);
        btnShowTableCard = findViewById(R.id.btn_eye);
        btnProbability = findViewById(R.id.btn_wahr);
        btnChooseOneCardFromDeck = findViewById(R.id.btn_dead);
        btnCheatingAlarm = findViewById(R.id.btn_cheatingalarm);
    }


    //Hides All ShowTheCheater Buttons if Cheating is not allowed

    public void hideCheatButtons() {
        if(!isCheatingAllowed) {
            btnCheatingAlarm.setVisibility(View.GONE);
            btnCheat.setVisibility(View.GONE);
            btnShowTableCard.setVisibility(View.GONE);
            btnProbability.setVisibility(View.GONE);
            btnChooseOneCardFromDeck.setVisibility(View.GONE);
        }
    }

    //Shows The ShowTheCheater Buttons if Cheating is allowed
    public void setCheatButtonsVisible () {
        if(isCheatingAllowed) {
            btnCheatingAlarm.setVisibility(View.VISIBLE);
            btnShowTableCard.setVisibility(View.GONE);
            btnProbability.setVisibility(View.GONE);
            btnChooseOneCardFromDeck.setVisibility(View.GONE);

            btnCheat.setVisibility(View.VISIBLE);
            btnCheat.setOnClickListener(new View.OnClickListener() {

                // onClick on the "ShowTheCheater"-Button the showTheCheater-option can be chose
                @Override
                public void onClick(View view) {
                    if (cheatOptionsVisible) {
                        cheatOptionsVisible = false;
                        btnShowTableCard.setVisibility(View.INVISIBLE);
                        btnProbability.setVisibility(View.INVISIBLE);
                        btnChooseOneCardFromDeck.setVisibility(View.INVISIBLE);
                    } else {
                        cheatOptionsVisible = true;
                        btnShowTableCard.setVisibility(View.VISIBLE);
                        btnProbability.setVisibility(View.VISIBLE);
                        btnChooseOneCardFromDeck.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    //Creates a dialog with the activePlayerNames and then calls the "probCheat"-Method in the "ProbCheating"-Class to find out
    //the right winning-probability of the choosen player
    public void testProbability() {

        ImageView flop3 = findViewById(R.id.flop3);
        ImageView turn = findViewById(R.id.turn);

        //Cheat-Funktion is just for this round

            btnProbability.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final ArrayList<Card> openedCards = new ArrayList<>(5);
                    final ArrayList<Card> interList = new ArrayList<>();
                    //Adding the Playernames to our Array for the Dialog
                    addingPlayerNamesToArray();

                    btnShowTableCard.setVisibility(View.GONE);
                    btnProbability.setVisibility(View.GONE);
                    btnChooseOneCardFromDeck.setVisibility(View.GONE);

                    AlertDialog.Builder createDialog = new AlertDialog.Builder(GameActivity.this);
                    createDialog.setTitle("Choose the player you want");
                    createDialog.setSingleChoiceItems(playerNames, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int indexPosition) {

                            if (PartyPokerApplication.isHost()&&Game.getInstance().getCommunityCards().get(0)!=null) {

                                openedCards.add(Game.getInstance().getPlayerByName(playerNames[indexPosition]).getCard1());
                                openedCards.add(Game.getInstance().getPlayerByName(playerNames[indexPosition]).getCard2());
                                openedCards.add(Game.getInstance().getCommunityCards().get(0));
                                openedCards.add(Game.getInstance().getCommunityCards().get(1));
                                openedCards.add(Game.getInstance().getCommunityCards().get(2));
                            } else if ((!PartyPokerApplication.isHost())&&communityCards.get(0)!=null) {
                                openedCards.add(getPlayerByName(playerNames[indexPosition]).getCard1());
                                openedCards.add(getPlayerByName(playerNames[indexPosition]).getCard2());
                                openedCards.add(communityCards.get(0));
                                openedCards.add(communityCards.get(1));
                                openedCards.add(communityCards.get(2));
                            }

                            interList.addAll(openedCards);
                            if(PartyPokerApplication.isHost()&&Game.getInstance().getCommunityCards().get(3)!=null) {
                                interList.add(Game.getInstance().getCommunityCards().get(3));
                            } else if ((!PartyPokerApplication.isHost())&&communityCards.get(3)!=null) {
                                interList.add(communityCards.get(3));
                            }

                            dialogInterface.dismiss();

                            if(openedCards.size()==5) {
                                ProbCheating probCheating = new ProbCheating();
                                String probResult = probCheating.probCheat(openedCards);
                                Toast.makeText(GameActivity.this, probResult, Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    final AlertDialog findProbOfPlayerX = createDialog.create();  //Dialog is beeing created


                    //Try to get the right round
                    if(interList.size()==5) {
                        findProbOfPlayerX.show();
                    }


                }
            });
        }

}