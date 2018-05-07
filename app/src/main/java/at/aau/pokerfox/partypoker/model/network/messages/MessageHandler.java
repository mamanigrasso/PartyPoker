package at.aau.pokerfox.partypoker.model.network.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDevice;

import java.io.IOException;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.model.network.messages.client.ActionMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.InitGameMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.NewCardMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.PlayerRolesMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.UpdateTableMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.WonAmountMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.YourTurnMessage;

public class MessageHandler implements SalutDataCallback {

    public void sendMessageToDevice(@NonNull final AbstractMessage message, @Nullable SalutDevice destinationDevice) {
        Salut network = PartyPokerApplication.getNetwork();
        network.sendToDevice(destinationDevice, message, new InnerSalutCallback(message));
    }

    public void sendMessageToAllClients(@NonNull final AbstractMessage message) {
        Salut network = PartyPokerApplication.getNetwork();
        network.sendToAllDevices(message, new InnerSalutCallback(message));
    }

    public void sendMessageToHost(@NonNull final AbstractMessage message) {
        Salut network = PartyPokerApplication.getNetwork();
        network.sendToHost(message, new InnerSalutCallback(message));
    }

    private void handleMessage(String json) {
        AbstractMessage message = parseJsonToMessage(json);

        if (message == null) {
            throw new RuntimeException("Message not supported!");
        }

        Bundle extras = new Bundle();

        if (message instanceof ActionMessage) {
            ActionMessage actionMessage = (ActionMessage) message;
            extras.putInt(BroadcastKeys.AMOUNT, actionMessage.Amount);
            extras.putBoolean(BroadcastKeys.HAS_FOLDED, actionMessage.HasFolded);
            extras.putBoolean(BroadcastKeys.IS_ALL_IN, actionMessage.IsAllIn);

            sendBroadcast(Broadcasts.ACTION_MESSAGE, extras);
        } else if (message instanceof InitGameMessage) {
            InitGameMessage gameMessage = (InitGameMessage) message;
            extras.putParcelableArrayList(BroadcastKeys.PLAYERS, gameMessage.Players);
            extras.putBoolean(BroadcastKeys.CHEAT_ON, gameMessage.IsCheatingAllowed);
            extras.putInt(BroadcastKeys.BIG_BLIND, gameMessage.SmallBlind);
            extras.putInt(BroadcastKeys.PLAYER_POT, gameMessage.PlayerPot);

            sendBroadcast(Broadcasts.INIT_GAME_MESSAGE, extras);
        } else if (message instanceof NewCardMessage) {
            NewCardMessage cardMessage = (NewCardMessage) message;
            extras.putParcelable(BroadcastKeys.CARD, cardMessage.NewHandCard);

            sendBroadcast(Broadcasts.NEW_CARD_MESSAGE, extras);
        } else if (message instanceof PlayerRolesMessage) {
            PlayerRolesMessage rolesMessage = (PlayerRolesMessage) message;
            extras.putBoolean(BroadcastKeys.IS_SMALL_BLIND, rolesMessage.IsSmallBlind);
            extras.putBoolean(BroadcastKeys.IS_BIG_BLIND, rolesMessage.IsBigBlind);
            extras.putBoolean(BroadcastKeys.IS_DEALER, rolesMessage.IsDealer);

            sendBroadcast(Broadcasts.PLAYER_ROLES_MESSAGE, extras);
        } else if (message instanceof WonAmountMessage) {
            WonAmountMessage amountMessage = (WonAmountMessage) message;
            extras.putInt(BroadcastKeys.AMOUNT, amountMessage.Amount);

            sendBroadcast(Broadcasts.WON_AMOUNT_MESSAGE, extras);
        } else if (message instanceof YourTurnMessage) {
            YourTurnMessage turnMessage = (YourTurnMessage) message;
            extras.putInt(BroadcastKeys.MIN_AMOUNT_TO_RAISE, turnMessage.MinAmountToRaise);

            sendBroadcast(Broadcasts.YOUR_TURN_MESSAGE, extras);
        } else if (message instanceof UpdateTableMessage) {
            UpdateTableMessage tableMessage = (UpdateTableMessage) message;
            extras.putParcelableArrayList(BroadcastKeys.CARDS, tableMessage.CommunityCards);
            extras.putParcelableArrayList(BroadcastKeys.PLAYERS, tableMessage.Players);
            extras.putInt(BroadcastKeys.NEW_POT, tableMessage.NewPotSize);

            sendBroadcast(Broadcasts.UPDATE_TABLE_MESSAGE, extras);
        } else {
            throw new RuntimeException("Unknown message type!");
        }
    }

    @Override
    public void onDataReceived(Object o) {

        if (o == null || o.equals("")) {
            throw new IllegalArgumentException("Json is null!");
        }

        handleMessage(o.toString());
    }

    private class InnerSalutCallback implements SalutCallback {
        AbstractMessage message;

        public InnerSalutCallback(AbstractMessage message) {
            this.message = message;
        }

        @Override
        public void call() {
            Toast.makeText(PartyPokerApplication.getAppContext(), "Sending of " + message.toString() + " " +
                    "went wrong.", Toast.LENGTH_LONG).show();
        }
    }

    private static void sendBroadcast(@NonNull String action, @Nullable Bundle extras) {
        Intent intentToSend = new Intent(action);
        if (extras != null) intentToSend.putExtras(extras);

        Context context = PartyPokerApplication.getAppContext();
        context.sendBroadcast(intentToSend);
    }

    /**
     * Don't look. LOOK AWAY!!!
     * @param json String
     * @return parsed message
     */
    private static AbstractMessage parseJsonToMessage(String json) {
        AbstractMessage message = null;

        message = parseJsonToMessageClass(json, ActionMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, InitGameMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, NewCardMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, PlayerRolesMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, UpdateTableMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, WonAmountMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, YourTurnMessage.class);
        if (message != null)
            return message;

        return null;
    }

    private static AbstractMessage parseJsonToMessageClass(String json, Class<? extends AbstractMessage> c) {
        AbstractMessage message = null;
        try {
            message = LoganSquare.parse(json, c);
        } catch (IOException e) { }

        return message;
    }
}
