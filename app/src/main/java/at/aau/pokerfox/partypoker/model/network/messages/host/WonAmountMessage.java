package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

@JsonObject
public class WonAmountMessage extends AbstractMessage {

    public WonAmountMessage() {
        this.MessageType = MessageTypes.WON_AMOUNT;
    }

    @JsonField
    public Integer Amount;
}
