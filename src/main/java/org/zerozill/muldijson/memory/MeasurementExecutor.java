package org.zerozill.muldijson.memory;

import org.zerozill.muldijson.JsonParserBenchmark;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

public class MeasurementExecutor {

    static String loggerPrefix = null;

    public static void main(String[] args) {
        if (args.length != 4) {
            throw new InvalidParameterException("Need to specify all the argument.");
        }
        String behavior = args[0];
        String forkNo = args[1];
        int iter = Integer.parseInt(args[2]);
        String path = args[3];
        loggerPrefix = "<" + behavior + ", " + JsonParserBenchmark.getInputGeneratorName() + ", fork: " + forkNo + ">";
        try {
            Class<JsonParserBenchmark> clazz = JsonParserBenchmark.class;
            Method method = clazz.getDeclaredMethod(behavior);
            Runtime runtime = Runtime.getRuntime();

            JsonParserBenchmark.setUp();
            System.gc();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

            for (int i = 0; i < iter; i++) {
                Object obj = method.invoke(null);
            }

            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            JsonParserBenchmark.tearDown();
            double memoryInKb = (afterMemory - beforeMemory) / 1024.0;
            System.out.println("# Memory Measurement, Fork: " + forkNo + ", Memory: " + memoryInKb + " KB");
            write(behavior, JsonParserBenchmark.getInputGeneratorName(), memoryInKb, path, forkNo);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(loggerPrefix + "No such method or illegal access.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    static void write(String name, String input, double data, String resultPath, String iter) {
        File csv = new File(resultPath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true));
            writer.write(String.format("%s,%s,%s,%s\n", name, input, iter, data));
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println(loggerPrefix + "Cannot find the specified path: " + resultPath);
        } catch (IOException e) {
            System.err.println(loggerPrefix + "Something wrong happened while writing.");
        }
    }
}
