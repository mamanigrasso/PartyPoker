package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;

@JsonObject
public class PlayerRolesMessage extends AbstractMessage {

    @JsonField
    public Boolean IsSmallBlind;

    @JsonField
    public Boolean IsBigBlind;

    @JsonField
    public Boolean IsDealer;
}
