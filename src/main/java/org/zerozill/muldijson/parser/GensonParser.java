package org.zerozill.muldijson.parser;

import com.owlike.genson.Genson;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GensonParser extends AbstractJsonParser {

    private Genson genson;

    public GensonParser() {
        this.parserType = Parsers.GENSON;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        if (genson == null) {
            genson = new Genson();
        }
        return genson.deserialize(json, clazz);
    }

    @Override
    public <T> String serialize(T bean) {
        if (genson == null) {
            genson = new Genson();
        }
        return genson.serialize(bean);
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
