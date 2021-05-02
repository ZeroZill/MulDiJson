package org.zerozill.muldijson.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class JacksonParser extends AbstractJsonParser {

    private ObjectMapper objectMapper;

    public JacksonParser() {
        this.parserType = Parsers.JACKSON;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        T pojo = null;
        try {
            pojo = objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return pojo;
    }

    @Override
    public <T> String serialize(T bean) {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
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
