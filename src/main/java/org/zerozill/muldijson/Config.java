package org.zerozill.muldijson;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Config {

    // ===================================
    // JMH
    // ===================================
    public int jmhForks;
    public int jmhWarmUpIterations;
    public int jmhMeasurementIterations;
    public long jmhMeasurementTime;
    public int jmhThreads;

    // ===================================
    // java.lang.Runtime
    // ===================================
    public int runtimeForks;
    public int runtimeMeasurementIterations;

    // ===================================
    // Other Options
    // ===================================
    public List<Method> includes;
    public InputMode inputMode;
    public String outputFileName;
    public int mutationTimes;

    Config() {
        init();
    }

    private void init() {
        jmhForks = 2;
        jmhWarmUpIterations = 5;
        jmhMeasurementIterations = 5;
        jmhMeasurementTime = 3;
        jmhThreads = 8;

        runtimeForks = 5;
        runtimeMeasurementIterations = 1;

        includes = Arrays.stream(JsonParserBenchmark.class.getDeclaredMethods()).filter(m -> m.getName().contains("test")).collect(Collectors.toList());
        inputMode = InputMode.CARRIAGE;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        outputFileName = simpleDateFormat.format(new Date());
        mutationTimes = 5;
    }

}