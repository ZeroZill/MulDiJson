package org.zerozill.muldijson;

import org.openjdk.jmh.annotations.*;
import org.zerozill.muldijson.factory.ParserFactory;
import org.zerozill.muldijson.input.CarriageGenerator;
import org.zerozill.muldijson.input.InputGenerator;
import org.zerozill.muldijson.input.UsersGenerator;
import org.zerozill.muldijson.parser.AbstractJsonParser;
import org.zerozill.muldijson.parser.Parsers;
import org.zerozill.muldijson.util.JsonObjectUtil;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
public class JsonParserBenchmark {
    static ParserFactory factory;
    static Config config;
    static InputGenerator inputGenerator;
    static Object object;
    static String json;
    static Map<Parsers, AbstractJsonParser> parsersMap;
    static Map<Parsers, Object> jsonObjectMap;

    public static String getInputGeneratorName() {
        if (inputGenerator == null) {
            return null;
        }
        return inputGenerator.toString();
    }

    @Setup
    public static void setUp() {
        factory = new ParserFactory();
        config = ConfigModel.getConfig();
        parsersMap = new HashMap<>();
        jsonObjectMap = new HashMap<>();
        inputGenerator = Generators.getInputGenerator(config.inputMode);
        object = inputGenerator.generateBean();
        json = inputGenerator.generateJson();

        for (Parsers p : Parsers.values()) {
            AbstractJsonParser parser = factory.getParser(p);
            if (parser.isNonBeanTypeParser()) {
                jsonObjectMap.put(p, JsonObjectUtil.getJsonObject(parser, json));
            }
            parsersMap.put(p, parser);
        }
    }

    @TearDown
    public static void tearDown() {

    }

    @Benchmark
    public static String test_FAST_JSON_SERIALIZE() {
        return parsersMap.get(Parsers.FAST_JSON).serialize(object);
    }


    @Benchmark
    public static Object test_FAST_JSON_DESERIALIZE() {
        return parsersMap.get(Parsers.FAST_JSON).deserialize(json, object.getClass());
    }


    @Benchmark
    public static String test_FAST_JSON_WRITE() {
        return parsersMap.get(Parsers.FAST_JSON).write(jsonObjectMap.get(Parsers.FAST_JSON));
    }


    @Benchmark
    public static Object test_FAST_JSON_PARSE() {
        return parsersMap.get(Parsers.FAST_JSON).parse(json);
    }


    @Benchmark
    public static String test_GSON_SERIALIZE() {
        return parsersMap.get(Parsers.GSON).serialize(object);
    }


    @Benchmark
    public static Object test_GSON_DESERIALIZE() {
        return parsersMap.get(Parsers.GSON).deserialize(json, object.getClass());
    }


    @Benchmark
    public static String test_GSON_WRITE() {
        return parsersMap.get(Parsers.GSON).write(jsonObjectMap.get(Parsers.GSON));
    }


    @Benchmark
    public static Object test_GSON_PARSE() {
        return parsersMap.get(Parsers.GSON).parse(json);
    }


    @Benchmark
    public static String test_ORG_JSON_WRITE() {
        return parsersMap.get(Parsers.ORG_JSON).write(jsonObjectMap.get(Parsers.ORG_JSON));
    }


    @Benchmark
    public static Object test_ORG_JSON_PARSE() {
        return parsersMap.get(Parsers.ORG_JSON).parse(json);
    }


    @Benchmark
    public static String test_LOGAN_SQUARE_SERIALIZE() {
        return parsersMap.get(Parsers.LOGAN_SQUARE).serialize(object);
    }


    @Benchmark
    public static Object test_LOGAN_SQUARE_DESERIALIZE() {
        return parsersMap.get(Parsers.LOGAN_SQUARE).deserialize(json, object.getClass());
    }


    @Benchmark
    public static String test_GENSON_SERIALIZE() {
        return parsersMap.get(Parsers.GENSON).serialize(object);
    }


    @Benchmark
    public static Object test_GENSON_DESERIALIZE() {
        return parsersMap.get(Parsers.GENSON).deserialize(json, object.getClass());
    }


    @Benchmark
    public static String test_JACKSON_SERIALIZE() {
        return parsersMap.get(Parsers.JACKSON).serialize(object);
    }


    @Benchmark
    public static Object test_JACKSON_DESERIALIZE() {
        return parsersMap.get(Parsers.JACKSON).deserialize(json, object.getClass());
    }


    @Benchmark
    public static String test_MINIMAL_JSON_WRITE() {
        return parsersMap.get(Parsers.MINIMAL_JSON).write(jsonObjectMap.get(Parsers.MINIMAL_JSON));
    }


    @Benchmark
    public static Object test_MINIMAL_JSON_PARSE() {
        return parsersMap.get(Parsers.MINIMAL_JSON).parse(json);
    }


    @Benchmark
    public static String test_FLEXJSON_SERIALIZE() {
        return parsersMap.get(Parsers.FLEXJSON).serialize(object);
    }


    @Benchmark
    public static Object test_FLEXJSON_DESERIALIZE() {
        return parsersMap.get(Parsers.FLEXJSON).deserialize(json, object.getClass());
    }
}
