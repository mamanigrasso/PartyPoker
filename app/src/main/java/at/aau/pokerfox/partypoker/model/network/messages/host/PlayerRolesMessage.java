package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.Player;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

@JsonObject
public class PlayerRolesMessage extends AbstractMessage {

    public PlayerRolesMessage() {
        this.MessageType = MessageTypes.PLAYER_ROLES;
    }

    @JsonField
    public Boolean IsSmallBlind;

    @JsonField
    public Boolean IsBigBlind;

    @JsonField
    public Boolean IsDealer;
}
