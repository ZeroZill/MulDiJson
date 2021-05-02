package org.zerozill.muldijson.parser;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FlexJsonParser extends AbstractJsonParser {

    public FlexJsonParser() {
        this.parserType = Parsers.FLEXJSON;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        JSONDeserializer<T> deserializer = new JSONDeserializer<>();
        return deserializer.deserialize(json, clazz);
    }

    @Override
    public <T> String serialize(T bean) {
        JSONSerializer serializer = new JSONSerializer();
        return serializer.serialize(bean);
    }

    @Deprecated
    @Override
    public <T> T parse(String json) {
//        return (T) new JSONTokener(json).nextValue();
        throw new NotImplementedException();
    }

    @Deprecated
    @Override
    public <T> String write(T jsonObject) {
        throw new NotImplementedException();
    }
}
