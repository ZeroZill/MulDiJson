package org.zerozill.muldijson.parser;

import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserClassification {

    public static final int BEAN_TYPE = 0;

    public static final int NON_BEAN_TYPE = 1;

    public static final Set<Parsers> BeanTypeParsers = Stream.of(
            Parsers.FAST_JSON,
            Parsers.FLEXJSON,
            Parsers.GENSON,
            Parsers.GSON,
            Parsers.JACKSON,
            Parsers.LOGAN_SQUARE
    ).collect(Collectors.toCollection(HashSet::new));

    public static final Set<Parsers> NonBeanTypeParsers = Stream.of(
            Parsers.FAST_JSON,
            Parsers.GSON,
            Parsers.MINIMAL_JSON,
            Parsers.ORG_JSON
    ).collect(Collectors.toCollection(HashSet::new));


    public static boolean isType(Parsers parser, int type) {
        Set<Parsers> parsersOfTheType;
        switch (type) {
            case BEAN_TYPE:
                parsersOfTheType = BeanTypeParsers;
                break;
            case NON_BEAN_TYPE:
                parsersOfTheType = NonBeanTypeParsers;
                break;
            default:
                throw new InvalidParameterException("Requires a valid type integer.");
        }

        return parsersOfTheType.contains(parser);
    }
}
