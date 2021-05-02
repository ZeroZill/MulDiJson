package org.zerozill.muldijson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class FastJsonParser extends AbstractJsonParser {

    public FastJsonParser() {
        this.parserType = Parsers.FAST_JSON;
    }

    @Override
    public <T> T deserialize(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    @Override
    public <T> String serialize(T bean) {
        return JSON.toJSONString(bean);
    }

    @Override
    public <T> T parse(String json) {
        return (T) JSON.parseObject(json);
    }

    @Override
    public <T> String write(T jsonObject) {
        assert jsonObject instanceof JSONObject;
        return ((JSONObject) jsonObject).toJSONString();
    }
}
