package at.aau.pokerfox.partypoker.model.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDevice;

import java.io.IOException;

import at.aau.pokerfox.partypoker.PartyPokerApplication;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.client.ActionMessage;
import at.aau.pokerfox.partypoker.model.network.messages.client.CheatPenaltyMessage;
import at.aau.pokerfox.partypoker.model.network.messages.client.ReplaceCardMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.InitGameMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.NewCardMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.ShowWinnerMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.UpdateTableMessage;
import at.aau.pokerfox.partypoker.model.network.messages.host.YourTurnMessage;

public class MessageHandler implements SalutDataCallback {

    public void sendMessageToDevice(@NonNull final AbstractMessage message, @Nullable SalutDevice destinationDevice) {
        Salut network = PartyPokerApplication.getNetwork();

        if (network != null)
            network.sendToDevice(destinationDevice, message, new InnerSalutCallback(message));
    }

    public void sendMessageToAllClients(@NonNull final AbstractMessage message) {
        Salut network = PartyPokerApplication.getNetwork();

        if (network != null)
            network.sendToAllDevices(message, new InnerSalutCallback(message));
    }

    public void sendMessageToHost(@NonNull final AbstractMessage message) {
        Salut network = PartyPokerApplication.getNetwork();

        if (network != null)
            network.sendToHost(message, new InnerSalutCallback(message));
    }

    private void handleMessage(String json) {
        AbstractMessage message = null;

        try {
            message = LoganSquare.parse(json, AbstractMessage.class);

            if (message == null) {
                throw new RuntimeException("Message not supported!");
            }

            Bundle extras = new Bundle();

            switch (message.MessageType) {
                case ACTION:
                    ActionMessage actionMessage = LoganSquare.parse(json, ActionMessage.class);
                    extras.putInt(BroadcastKeys.AMOUNT, actionMessage.Amount);
                    extras.putBoolean(BroadcastKeys.HAS_FOLDED, actionMessage.HasFolded);

                    sendBroadcast(Broadcasts.ACTION_MESSAGE, extras);
                    break;

                case REPLACE_CARD:
                    ReplaceCardMessage replaceCardMessage = LoganSquare.parse(json, ReplaceCardMessage.class);
                    extras.putParcelable(BroadcastKeys.REPLACEMENT_CARD, replaceCardMessage.replacementCard);
                    extras.putBoolean(BroadcastKeys.CARD_TO_REPLACE, replaceCardMessage.replaceCard1);

                    sendBroadcast(Broadcasts.REPLACE_CARD_MESSAGE, extras);
                    break;

                case CHEAT_PENALTY:
                    CheatPenaltyMessage cheatPenaltyMessage = LoganSquare.parse(json, CheatPenaltyMessage.class);
                    extras.putString(BroadcastKeys.COMPLAINER, cheatPenaltyMessage.complainer);
                    extras.putString(BroadcastKeys.CHEATER, cheatPenaltyMessage.cheater);
                    extras.putBoolean(BroadcastKeys.PENALIZECHEATER, cheatPenaltyMessage.penalizeCheater);

                    sendBroadcast(Broadcasts.CHEAT_PENALTY_MESSAGE, extras);
                    break;

                case INIT_GAME:
                    InitGameMessage gameMessage = LoganSquare.parse(json, InitGameMessage.class);
                    extras.putParcelableArrayList(BroadcastKeys.PLAYERS, gameMessage.Players);
                    extras.putBoolean(BroadcastKeys.CHEAT_ON, gameMessage.IsCheatingAllowed);
                    extras.putInt(BroadcastKeys.BIG_BLIND, gameMessage.SmallBlind);
                    extras.putInt(BroadcastKeys.PLAYER_POT, gameMessage.PlayerPot);

                    sendBroadcast(Broadcasts.INIT_GAME_MESSAGE, extras);
                    break;

                case NEW_CARD:
                    NewCardMessage cardMessage = LoganSquare.parse(json, NewCardMessage.class);
                    extras.putParcelable(BroadcastKeys.CARD, cardMessage.NewHandCard);

                    sendBroadcast(Broadcasts.NEW_CARD_MESSAGE, extras);
                    break;

                case YOUR_TURN:
                    YourTurnMessage turnMessage = LoganSquare.parse(json, YourTurnMessage.class);
                    extras.putInt(BroadcastKeys.MIN_AMOUNT_TO_RAISE, turnMessage.MinAmountToRaise);

                    sendBroadcast(Broadcasts.YOUR_TURN_MESSAGE, extras);
                    break;

                case SHOW_WINNER:
                    ShowWinnerMessage showWinnerMessage = LoganSquare.parse(json, ShowWinnerMessage.class);
                    extras.putString(BroadcastKeys.WINNER_INFO, showWinnerMessage.WinnerInfo);
                    extras.putBoolean(BroadcastKeys.FINAL_WINNER, showWinnerMessage.FinalWinner);

                    sendBroadcast(Broadcasts.SHOW_WINNER_MESSAGE, extras);
                    break;

                case UPDATE_TABLE:
                    UpdateTableMessage tableMessage = LoganSquare.parse(json, UpdateTableMessage.class);
                    extras.putParcelableArrayList(BroadcastKeys.CARDS, tableMessage.CommunityCards);
                    extras.putParcelableArrayList(BroadcastKeys.PLAYERS, tableMessage.Players);
                    extras.putInt(BroadcastKeys.NEW_POT, tableMessage.NewPotSize);

                    sendBroadcast(Broadcasts.UPDATE_TABLE_MESSAGE, extras);
                    break;

                    default:
                        throw new RuntimeException("Unknown message type!");
            }
        } catch (IOException e) {
            Log.e("exception", "could not parse json (" + json + "): " + e.getMessage());
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
            Log.e("sendinWentWrong", message.toString());
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

        message = parseJsonToMessageClass(json, ReplaceCardMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, CheatPenaltyMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, InitGameMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, NewCardMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, UpdateTableMessage.class);
        if (message != null)
            return message;

        message = parseJsonToMessageClass(json, ShowWinnerMessage.class);
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
            Gson gson = new Gson();
            message = gson.fromJson(json, c);
        } catch (JsonSyntaxException e) {
            Log.e("parseJsonToMessageClass", e.getMessage());
        }

        return message;
    }
}
