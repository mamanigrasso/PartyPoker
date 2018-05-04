package at.aau.pokerfox.partypoker.model.network.messages.client;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;

@JsonObject
public class ActionMessage extends AbstractMessage {

    @JsonField
    public Integer Amount;

    @JsonField
    public Boolean HasFolded;

    @JsonField
    public Boolean IsAllIn;
}
