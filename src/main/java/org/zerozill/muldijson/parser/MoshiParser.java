package org.zerozill.muldijson.parser;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MoshiParser extends AbstractJsonParser {

    public MoshiParser() {
        this.parserType = Parsers.MOSHI;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        throw new NotImplementedException();
    }

    @Override
    public <T> String serialize(T bean) {
        throw new NotImplementedException();
    }

    @Deprecated
    @Override
    public <T> T parse(String json) {
        throw new NotImplementedException();
    }

    @Deprecated
    @Override
    public <T> String write(T jsonObject) {
        throw new NotImplementedException();
    }
}
