package org.zerozill.muldijson.parser;

import org.zerozill.muldijson.behavior.Deserializable;
import org.zerozill.muldijson.behavior.Parsable;
import org.zerozill.muldijson.behavior.Serializable;
import org.zerozill.muldijson.behavior.Writable;

public abstract class AbstractJsonParser implements Serializable, Deserializable, Parsable, Writable {
    public Parsers parserType;

    public boolean isBeanTypeParser() {
        return ParserClassification.isType(parserType, ParserClassification.BEAN_TYPE);
    }

    public boolean isNonBeanTypeParser() {
        return ParserClassification.isType(parserType, ParserClassification.NON_BEAN_TYPE);
    }
}
