package org.zerozill.muldijson.validation;

import org.zerozill.muldijson.factory.ParserFactory;
import org.zerozill.muldijson.parser.AbstractJsonParser;
import org.zerozill.muldijson.parser.ParserClassification;
import org.zerozill.muldijson.parser.Parsers;
import org.zerozill.muldijson.util.JsonObjectUtil;

import java.security.InvalidParameterException;

/**
 * Validator for Non-Bean Type Parser (Parsers who cannot deserialize Json to Bean)
 */
public class NonBeanTypeParserValidator extends ParserValidator {

    public NonBeanTypeParserValidator(AbstractJsonParser parser) {
        if (!ParserClassification.isType(parser.parserType, ParserClassification.NON_BEAN_TYPE)) {
            throw new InvalidParameterException("\"" + parser.parserType + "\" is not Non-Bean type.");
        }
        this.parser = parser;
    }

    public NonBeanTypeParserValidator(Parsers parserType) {
        if (!ParserClassification.isType(parserType, ParserClassification.NON_BEAN_TYPE)) {
            throw new InvalidParameterException("\"" + parserType + "\" is not Non-Bean type.");
        }
        ParserFactory factory = new ParserFactory();
        this.parser = factory.getParser(parserType);
    }

    @Override
    public <T> ValidationResult isSameDeserializationResult(String json, T targetObject) throws NullPointerException {
        if (this.parser == null) {
            throw new NullPointerException("Parser need to be specified.");
        }
        Object object = this.parser.parse(json);
        return isSameDeserializationResult(object, targetObject);
    }

    @Override
    public <E, T> ValidationResult isSameDeserializationResult(E object, T targetObject) throws NullPointerException {
        if (parser == null) {
            throw new NullPointerException("Parser need to be specified.");
        }

        if (!ParserClassification.isType(parser.parserType, ParserClassification.NON_BEAN_TYPE)) {
            throw new InvalidParameterException("Not a Non-Bean type parser.");
        }

        Parsers targetParserType = JsonObjectUtil.getNonBeanParserType(targetObject);
        if (targetParserType == null) {
            throw new NullPointerException("Invalid target object : " + targetObject);
        }

        ParserFactory factory = new ParserFactory();
        AbstractJsonParser jsonParser = factory.getParser(this.parser.parserType);
        AbstractJsonParser targetJsonParser = factory.getParser(targetParserType);
        String json = jsonParser.write(object);
        String targetJson = targetJsonParser.write(targetObject);
        return isSameSerializationResult(json, targetJson);
    }
}
