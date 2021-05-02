package org.zerozill.muldijson.util;

import com.alibaba.fastjson.JSONObject;
import com.eclipsesource.json.JsonValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.zerozill.muldijson.parser.AbstractJsonParser;
import org.zerozill.muldijson.parser.ParserClassification;
import org.zerozill.muldijson.parser.Parsers;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonObjectUtil {

    public static Object get(Object jsonObject, String key, Parsers parser) {
        if (!ParserClassification.isType(parser, ParserClassification.NON_BEAN_TYPE)) {
            throw new InvalidParameterException("Not a Non-Bean type parser.");
        }
        switch (parser) {
            case FAST_JSON:
                return ((JSONObject) jsonObject).get(key);
            case GSON:
                return ((JsonObject) jsonObject).get(key);
            case MINIMAL_JSON:
                return ((JsonValue) jsonObject).asObject().get(key);
            case ORG_JSON:
                return ((org.json.JSONObject) jsonObject).get(key);
            default:
                throw new InvalidParameterException("Not a Non-Bean type parser.");
        }
    }

    public static Set<String> getKeySet(Object object, Parsers parser) {
        switch (parser) {
            case FAST_JSON:
                return ((JSONObject) object).keySet();
            case GSON:
                return ((JsonObject) object).keySet();
            case MINIMAL_JSON:
                List<String> names = ((JsonValue) object).asObject().names();
                return new HashSet<>(names);
            case ORG_JSON:
                return ((org.json.JSONObject) object).keySet();
            default:
                return null;
        }
    }

    public static <T> Parsers getNonBeanParserType(T object) {
        Class clazz = object.getClass();
        if (clazz == JSONObject.class) {
            return Parsers.FAST_JSON;
        } else if (clazz == JsonObject.class) {
            return Parsers.GSON;
        } else if (clazz == com.eclipsesource.json.JsonObject.class) {
            return Parsers.MINIMAL_JSON;
        } else if (clazz == org.json.JSONObject.class) {
            return Parsers.ORG_JSON;
        } else {
            return null;
        }
    }

    public static Object getJsonObject(AbstractJsonParser parser, String json) {
        switch (parser.parserType) {
            case FAST_JSON:
                return ((JSONObject) parser.parse(json));
            case GSON:
                return ((JsonObject) parser.parse(json));
            case MINIMAL_JSON:
                return ((JsonValue) parser.parse(json));
            case ORG_JSON:
                return ((org.json.JSONObject) parser.parse(json));
            default:
                throw new InvalidParameterException("Not a Non-Bean type parser.");
        }
    }
}
