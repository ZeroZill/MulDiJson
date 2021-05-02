package org.zerozill.muldijson.parser;

import com.bluelinelabs.logansquare.LoganSquare;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

public class LoganSquareParser extends AbstractJsonParser {

    public LoganSquareParser() {
        this.parserType = Parsers.LOGAN_SQUARE;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        T bean = null;
        try {
            bean = LoganSquare.parse(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public <T> String serialize(T bean) {
        String json = null;
        try {
            json = LoganSquare.serialize(bean);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
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
