package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;
import java.util.LinkedList;

import at.aau.pokerfox.partypoker.model.Player;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

@JsonObject
public class InitGameMessage extends AbstractMessage {

    public InitGameMessage() {
        this.MessageType = MessageTypes.INIT_GAME;
    }

    @JsonField
    public ArrayList<Player> Players;

    @JsonField
    public Boolean IsCheatingAllowed;

    @JsonField
    public Integer SmallBlind;

    @JsonField
    public Integer PlayerPot;
}
