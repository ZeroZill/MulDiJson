package org.zerozill.muldijson.parser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GsonParser extends AbstractJsonParser {
    private Gson gson;

    public GsonParser() {
        this.parserType = Parsers.GSON;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(json, clazz);
    }

    @Override
    public <T> String serialize(T bean) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(bean);
    }

    @Deprecated
    @Override
    public <T> T parse(String json) {
        return (T) JsonParser.parseString(json).getAsJsonObject();
    }

    @Deprecated
    @Override
    public <T> String write(T jsonObject) {
        assert jsonObject instanceof JsonElement;
        return jsonObject.toString();
    }
}
