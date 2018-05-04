package at.aau.pokerfox.partypoker.model.network.messages.host;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.ArrayList;

import at.aau.pokerfox.partypoker.model.Card;
import at.aau.pokerfox.partypoker.model.Player;
import at.aau.pokerfox.partypoker.model.network.messages.AbstractMessage;

@JsonObject
public class UpdateTableMessage extends AbstractMessage {

    @JsonField
    public ArrayList<Card> CommunityCards;

    @JsonField
    public ArrayList<Player> Players;

    @JsonField
    public Integer NewPotSize;
}
