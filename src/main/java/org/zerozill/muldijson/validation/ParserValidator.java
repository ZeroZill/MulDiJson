package org.zerozill.muldijson.validation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.zerozill.muldijson.factory.ParserFactory;
import org.zerozill.muldijson.parser.AbstractJsonParser;
import org.zerozill.muldijson.parser.Parsers;
import org.zerozill.muldijson.util.CompareUtil;

public abstract class ParserValidator implements SerializationValidator, DeserializationValidator {

    protected AbstractJsonParser parser;

    public ParserValidator() {
        parser = null;
    }

    public ParserValidator(AbstractJsonParser parser) {
        this.parser = parser;
    }

    public ParserValidator(Parsers parser) {
        ParserFactory factory = new ParserFactory();
        this.parser = factory.getParser(parser);
    }

    public AbstractJsonParser getParser() {
        return parser;
    }

    public void setParser(AbstractJsonParser parser) {
        this.parser = parser;
    }

    /* Use GSON to parse the json and compare the result. */
    @Override
    public <T> ValidationResult isSameSerializationResult(T object, String targetJson) throws NullPointerException {
        if (this.parser == null) {
            throw new NullPointerException("Parser need to be specified.");
        }
        String json = parser.write(object);
        return isSameSerializationResult(json, targetJson);
    }

    /* Use GSON to parse the json and compare the result. */
    @Override
    public <T> ValidationResult isSameSerializationResult(String json, String targetJson) throws NullPointerException {
        ValidationResult vResult = new ValidationResult();
        JsonObject object1 = (JsonObject) JsonParser.parseString(json);
        JsonObject object2 = (JsonObject) JsonParser.parseString(targetJson);

        // remove fields added by GroovyClassLoader
        object1.remove("$staticClassInfo");
        object1.remove("__$stMC");
        object1.remove("metaClass");
        object1.remove("$callSiteArray");
        object2.remove("$staticClassInfo");
        object2.remove("__$stMC");
        object2.remove("metaClass");
        object2.remove("$callSiteArray");

        // remove fields added by flexjson
        object1.remove("class");
        object2.remove("class");

        boolean isSame = object1.equals(object2);
        vResult.setSame(isSame);
        if (!isSame) {
            vResult.setDetail(CompareUtil.getDifferences(object1, object2));
        }
        return vResult;
    }

}
