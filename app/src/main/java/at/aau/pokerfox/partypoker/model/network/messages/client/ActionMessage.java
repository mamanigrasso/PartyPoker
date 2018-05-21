package at.aau.pokerfox.partypoker.model.network.messages.client;

import android.drm.DrmStore;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

@JsonObject
public class ActionMessage extends AbstractMessage {

    public ActionMessage() {
        this.MessageType = MessageTypes.ACTION;
    }

    @JsonField
    public Integer Amount;

    @JsonField
    public Boolean HasFolded;

    @JsonField
    public Boolean IsAllIn;
}
