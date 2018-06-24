package at.aau.pokerfox.partypoker.model.network.messages.client;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

/**
 * Created by Manuel on 09.06.2018.
 */

@JsonObject
public class CheatPenaltyMessage extends AbstractMessage {
    public CheatPenaltyMessage() {
        this.MessageType = MessageTypes.CHEAT_PENALTY;
    }

    @JsonField
    public String complainer;

    @JsonField
    public String cheater;

    @JsonField
    public Boolean penalizeCheater;
}
