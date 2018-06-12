package at.aau.pokerfox.partypoker.model.network.messages.client;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.Card;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

/**
 * Created by Manuel on 08.06.2018.
 */

@JsonObject
public class ReplaceCardMessage extends AbstractMessage {
    public ReplaceCardMessage() {
        this.MessageType = MessageTypes.REPLACE_CARD;
    }

    @JsonField
    public Card replacementCard;

    @JsonField
    public Boolean replaceCard1;
}
