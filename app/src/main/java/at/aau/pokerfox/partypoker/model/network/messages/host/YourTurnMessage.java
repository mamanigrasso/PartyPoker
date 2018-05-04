package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;

@JsonObject
public class YourTurnMessage extends AbstractMessage {

    @JsonField
    public Integer MinAmountToRaise;
}
