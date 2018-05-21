package at.aau.pokerfox.partypoker.model.network.messages;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

import at.aau.pokerfox.partypoker.model.network.typeadapters.IntEnumTypeConverter;

@JsonObject
public class AbstractMessage implements Serializable {

    @JsonField (typeConverter = IntEnumTypeConverter.class)
    public MessageTypes MessageType;
}
