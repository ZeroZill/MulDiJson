package org.zerozill.muldijson.parser;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MinimalJsonParser extends AbstractJsonParser {

    public MinimalJsonParser() {
        this.parserType = Parsers.MINIMAL_JSON;
    }

    @Deprecated
    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        throw new NotImplementedException();
    }

    @Deprecated
    @Override
    public <T> String serialize(T bean) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T parse(String json) {
        return (T) Json.parse(json);
    }

    @Override
    public <T> String write(T jsonObject) {
        assert  jsonObject instanceof JsonValue;
        return jsonObject.toString();
    }
}
