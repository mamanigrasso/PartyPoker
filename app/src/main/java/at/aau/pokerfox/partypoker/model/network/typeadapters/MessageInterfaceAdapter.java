package at.aau.pokerfox.partypoker.model.network.typeadapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public final class MessageInterfaceAdapter implements JsonDeserializer<MessageInterface>, JsonSerializer<MessageInterface> {
    private static final String PROP_NAME = "clazz";

    @Override
    public MessageInterface deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String classPath = json.getAsJsonObject().getAsJsonPrimitive(PROP_NAME).getAsString();
            Class<MessageInterface> cls = (Class<MessageInterface>) Class.forName(classPath);

            return (MessageInterface) context.deserialize(json, cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public JsonElement serialize(MessageInterface src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = context.serialize(src).getAsJsonObject();

        String classPath = src.getClass().getName();
        jo.add(PROP_NAME, new JsonPrimitive(classPath));

        return jo;
    }
}
