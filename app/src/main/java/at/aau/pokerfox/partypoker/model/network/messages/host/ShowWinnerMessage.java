package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;
import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

/**
 * Created by Manuel on 07.06.2018.
 */

@JsonObject
public class ShowWinnerMessage extends AbstractMessage {

    public ShowWinnerMessage() {
        this.MessageType = MessageTypes.SHOW_WINNER;
    }

    @JsonField
    public String WinnerInfo;
}

