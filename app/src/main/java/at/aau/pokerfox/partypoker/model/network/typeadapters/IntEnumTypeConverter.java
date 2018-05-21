package at.aau.pokerfox.partypoker.model.network.typeadapters;

import com.bluelinelabs.logansquare.typeconverters.IntBasedTypeConverter;

import java.util.Arrays;

import at.aau.pokerfox.partypoker.model.network.messages.MessageTypes;

public class IntEnumTypeConverter extends IntBasedTypeConverter<MessageTypes> {

    @Override
    public MessageTypes getFromInt(int i) {
        return MessageTypes.values()[i];
    }

    @Override
    public int convertToInt(MessageTypes object) {
        return Arrays.asList(MessageTypes.values()).indexOf(object);
    }
}
