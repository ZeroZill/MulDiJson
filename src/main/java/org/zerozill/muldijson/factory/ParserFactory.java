package org.zerozill.muldijson.factory;

import org.zerozill.muldijson.parser.*;

import java.security.InvalidParameterException;

public class ParserFactory {

    private final FastJsonParser fastJsonParser;
    private final GsonParser gsonParser;
    private final OrgJsonParser orgJsonParser;
    private final LoganSquareParser loganSquareParser;
    private final GensonParser gensonParser;
    private final JacksonParser jacksonParser;
    private final MoshiParser moshiParser;
    private final MinimalJsonParser minimalJsonParser;
    private final KlaxonParser klaxonParser;
    private final FlexJsonParser flexJsonParser;

    public ParserFactory() {
        fastJsonParser = null;
        gsonParser = null;
        orgJsonParser = null;
        loganSquareParser = null;
        gensonParser = null;
        jacksonParser = null;
        moshiParser = null;
        minimalJsonParser = null;
        klaxonParser = null;
        flexJsonParser = null;
    }

    public AbstractJsonParser getParser(Parsers parser) {
        switch (parser) {
            case FAST_JSON:
                return fastJsonParser == null ? new FastJsonParser() : fastJsonParser;
            case GSON:
                return gsonParser == null ? new GsonParser() : gsonParser;
            case ORG_JSON:
                return orgJsonParser == null ? new OrgJsonParser() : orgJsonParser;
            case LOGAN_SQUARE:
                return loganSquareParser == null ? new LoganSquareParser() : loganSquareParser;
            case GENSON:
                return gensonParser == null ? new GensonParser() : gensonParser;
            case JACKSON:
                return jacksonParser == null ? new JacksonParser() : jacksonParser;
            case MOSHI:
                return moshiParser == null ? new MoshiParser() : moshiParser;
            case MINIMAL_JSON:
                return minimalJsonParser == null ? new MinimalJsonParser() : minimalJsonParser;
            case KLAXON:
                return klaxonParser == null ? new KlaxonParser() : klaxonParser;
            case FLEXJSON:
                return flexJsonParser == null ? new FlexJsonParser() : flexJsonParser;
            default:
                throw new InvalidParameterException(
                        "\"" + parser + "\" is not a valid json parser name."
                );
        }
    }

}
