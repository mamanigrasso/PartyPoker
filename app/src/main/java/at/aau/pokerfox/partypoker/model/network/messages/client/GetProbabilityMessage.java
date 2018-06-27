package at.aau.pokerfox.partypoker.model.network.messages.client;

/**
 * Created by Andreas on 27.06.2018.
 */

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.Card;
import at.aau.pokerfox.partypoker.model.Player;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

@JsonObject
public class GetProbabilityMessage extends AbstractMessage {
    public GetProbabilityMessage() {
        this.MessageType = MessageTypes.GET_PROBABILITY;
    }

    @JsonField
    public Boolean clientCheatStatus;
}
