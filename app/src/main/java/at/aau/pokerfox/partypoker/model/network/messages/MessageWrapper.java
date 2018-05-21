package at.aau.pokerfox.partypoker.model.network.messages;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class MessageWrapper {

    @JsonField
    AbstractMessage message;

    public AbstractMessage getMessage() {
        return message;
    }

    public void setMessage(AbstractMessage message) {
        this.message = message;
    }
}
