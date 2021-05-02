package org.zerozill.muldijson.validation;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.zerozill.muldijson.factory.ParserFactory;
import org.zerozill.muldijson.parser.AbstractJsonParser;
import org.zerozill.muldijson.parser.ParserClassification;
import org.zerozill.muldijson.parser.Parsers;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;

/**
 * Validator for Bean Type Parser (Parsers who can deserialize Json to Bean)
 */
public class BeanTypeParserValidator extends ParserValidator {

    public BeanTypeParserValidator(AbstractJsonParser parser) {
        if (!ParserClassification.isType(parser.parserType, ParserClassification.BEAN_TYPE)) {
            throw new InvalidParameterException("\"" + parser.parserType + "\" is not Bean type.");
        }
        this.parser = parser;
    }

    public BeanTypeParserValidator(Parsers parserType) {
        if (!ParserClassification.isType(parserType, ParserClassification.BEAN_TYPE)) {
            throw new InvalidParameterException("\"" + parserType + "\" is not Bean type.");
        }
        ParserFactory factory = new ParserFactory();
        this.parser = factory.getParser(parserType);
    }

    @Override
    public <T> ValidationResult isSameDeserializationResult(String json, T targetObject) throws NullPointerException {
        if (this.parser == null) {
            throw new NullPointerException("Parser need to be specified.");
        }
        T object = (T) this.parser.deserialize(json, targetObject.getClass());
        return isSameDeserializationResult(object, targetObject);
    }

    @Override
    public <E, T> ValidationResult isSameDeserializationResult(E object, T targetObject) throws NullPointerException {
        if (object == null || targetObject == null) {
            throw new NullPointerException("Objects to be compared cannot be null.");
        }

        if (!object.getClass().isAssignableFrom(targetObject.getClass())) {
            throw new ClassCastException("Class of compared Bean type must be the same.");
        }

        ValidationResult result = new ValidationResult();

        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(object, targetObject);

        result.setSame(!diff.hasChanges());
        if (diff.hasChanges()) {
            result.setDetail(
                    "===================================================\n" +
                    diff.toString() +
                    "===================================================\n"
            );
        }
        return result;
    }
}
