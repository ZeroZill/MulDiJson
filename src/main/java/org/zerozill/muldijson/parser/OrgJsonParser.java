package org.zerozill.muldijson.parser;


import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class OrgJsonParser extends AbstractJsonParser {

    public OrgJsonParser() {
        this.parserType = Parsers.ORG_JSON;
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
        return (T) new JSONObject(json);
    }

    @Override
    public <T> String write(T jsonObject) {
        assert jsonObject instanceof JSONObject;
        return jsonObject.toString();
    }
}
