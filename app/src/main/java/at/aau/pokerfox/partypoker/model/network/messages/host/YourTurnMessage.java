package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

@JsonObject
public class YourTurnMessage extends AbstractMessage {

    public YourTurnMessage() {
        this.MessageType = MessageTypes.YOUR_TURN;
    }

    @JsonField
    public Integer MinAmountToRaise;
}
