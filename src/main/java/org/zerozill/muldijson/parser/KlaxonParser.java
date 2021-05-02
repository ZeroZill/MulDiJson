package org.zerozill.muldijson.parser;

import com.beust.klaxon.Klaxon;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class KlaxonParser extends AbstractJsonParser {

    private Klaxon klaxon;

    public KlaxonParser() {
        this.parserType = Parsers.KLAXON;
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
