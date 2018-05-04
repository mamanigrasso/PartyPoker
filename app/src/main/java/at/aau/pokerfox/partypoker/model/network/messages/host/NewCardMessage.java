package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.Card;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;

@JsonObject
public class NewCardMessage extends AbstractMessage {

    @JsonField
    public Card NewHandCard;
}
