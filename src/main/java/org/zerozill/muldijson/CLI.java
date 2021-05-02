package org.zerozill.muldijson;

import org.apache.commons.cli.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.zerozill.muldijson.input.InputGenerator;
import org.zerozill.muldijson.memory.MemoryMeasurer;
import org.zerozill.muldijson.validation.CorrectnessValidation;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class CLI {

    private static Options options;
    private static CommandLineParser commandLineParser;
    private static Config config;

    private static void buildArgs() {
        options = new Options();
        Option option;

        // ===================================
        // JMH
        // ===================================
        option = new Option("jf", "jmh_forks", true,
                "The number of processes to be tested in JMH.");
        options.addOption(option);

        option = new Option("jwi", "jmh_warmup_iterations", true,
                "The number of warmup iterations in JMH.");
        options.addOption(option);

        option = new Option("jmi", "jmh_measurement_iterations", true,
                "The number of measurement iterations in JMH.");
        options.addOption(option);

        option = new Option("jmt", "jmh_measurement_time", true,
                "The duration of measurement in JMH.");
        options.addOption(option);

        option = new Option("jt", "jmh_threads", true,
                "The number of testing threads in JMH.");
        options.addOption(option);

        // ===================================
        // java.lang.Runtime
        // ===================================
        option = new Option("rf", "runtime_forks", true,
                "The number of processes to be tested in memory testing.");
        options.addOption(option);

        option = new Option("rmi", "runtime_measurement_iterations", true,
                "The number of measurement iterations in memory testing.");
        options.addOption(option);

        // ===================================
        // Other Options
        // ===================================
        option = new Option("i", "includes", true,
                "A csv path that contains the behaviors to be tested.");
        options.addOption(option);

        option = new Option("m", "input_mode", true,
                "The mode of generating input.");
        options.addOption(option);

        option = new Option("o", "output_name", true,
                "The name of result csv file. " +
                        "The result of time measurement will be put in '/result/time/' directory " +
                        "and the result of memory measurement will be put in '/result/memory/' directory.");
        options.addOption(option);

        option = new Option("mt", "mutation_times", true,
                "The times of applying mutations.");
        options.addOption(option);
    }

    private static void init() {
        commandLineParser = new DefaultParser();
        config = ConfigModel.getConfig();

        // If "result/time/" or "result/memory/" not exist, then create them.
        Path timePath = Paths.get("result/time/");
        Path memoryPath = Paths.get("result/memory/");
        Path diffPath = Paths.get("result/diff");
        Path classPath = Paths.get("result/class");

        try {
            Files.createDirectories(timePath);
            Files.createDirectories(memoryPath);
            Files.createDirectories(diffPath);
            Files.createDirectories(classPath);
        } catch (IOException e) {
            System.err.println("IO exception happened when creating 'result/time/' and 'result/memory/' directories.\n" + e.getMessage());
        }
    }

    private static void parseArgs(String[] args) {
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            // ===================================
            // JMH
            // ===================================
            config.jmhForks = Integer.parseInt(commandLine.getOptionValue("jf", "2"));
            config.jmhWarmUpIterations = Integer.parseInt(commandLine.getOptionValue("jwi", "5"));
            config.jmhMeasurementIterations = Integer.parseInt(commandLine.getOptionValue("jmi", "5"));
            config.jmhMeasurementTime = Long.parseLong(commandLine.getOptionValue("jmt", "3"));
            config.jmhThreads = Integer.parseInt(commandLine.getOptionValue("jt", "8"));

            // ===================================
            // java.lang.Runtime
            // ===================================
            config.runtimeForks = Integer.parseInt(commandLine.getOptionValue("rf", "5"));
            config.runtimeMeasurementIterations = Integer.parseInt(commandLine.getOptionValue("rmi", "1"));

            // ===================================
            // Other Options
            // ===================================
            config.includes = ConfigModel.getIncludes(commandLine.getOptionValue("i", ""));
            config.inputMode = ConfigModel.getInputMode(commandLine.getOptionValue("m", "CARRIAGE"));
            config.outputFileName = commandLine.getOptionValue("o", "");
            config.mutationTimes = Integer.parseInt(commandLine.getOptionValue("mt", "5"));
        } catch (ParseException e) {
            System.err.println("No such argument.\n" + e.getMessage());
        }
    }


    public static void runTimeMeasurement() {

        String resultPath = "result/time/" + config.outputFileName + ".csv";

        ChainedOptionsBuilder opt = new OptionsBuilder()
                .warmupIterations(config.jmhWarmUpIterations)
                .measurementIterations(config.jmhMeasurementIterations)
                .timeUnit(TimeUnit.MICROSECONDS)
                .measurementTime(TimeValue.seconds(config.jmhMeasurementTime))
                .forks(config.jmhForks)
                .threads(config.jmhThreads)
                .result(resultPath)
                .resultFormat(ResultFormatType.CSV);

        for (Method m : config.includes) {
            boolean mutated = ConfigModel.isMutatedMode();
            if (m.getName().contains("LOGAN_SQUARE") && mutated) {
                System.err.println("[Time Measurement]Logan Square does not support runtime loaded class, here we skip it.");
            } else {
                String includeMethod = m.getName();
                opt.include(includeMethod);
            }
        }

        try {
            System.out.println("### Start measuring time consuming. ###");
            new Runner(opt.build()).run();
        } catch (RunnerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("### Time consuming measurement completed. ###");
        }
    }

    public static void runMemoryMeasurement() {
        MemoryMeasurer measurer = new MemoryMeasurer();
        String resultPath = "result/memory/" + config.outputFileName + ".csv";
        measurer.setForks(config.runtimeForks);
        measurer.setIterations(config.runtimeMeasurementIterations);
        measurer.setIncludes(config.includes);
        measurer.setResultPath(resultPath);

        System.out.println("### Start measuring memory consuming. ###");
        measurer.run();
        System.out.println("### Memory consuming measurement completed. ###");
    }

    public static void runDifferentialTesting() {
        CorrectnessValidation cv = new CorrectnessValidation();

        if (JsonParserBenchmark.json == null || JsonParserBenchmark.object == null) {
            JsonParserBenchmark.setUp();
        }

        String json = JsonParserBenchmark.json;
        Object object = JsonParserBenchmark.object;

        String resultPath = "result/diff/" + config.outputFileName + ".txt";

        System.out.println("### Start running Differential Testing. ###");

        File res = new File(resultPath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(res));

            writer.write("################################# Bean to Json ####################################\n");
            writer.write(cv.validateBeanToJson(object, object.getClass()) + "\n\n\n");
            writer.write("################################# Json to Bean ####################################\n");
            writer.write(cv.validateJsonToBean(json, object.getClass()) + "\n\n\n");
            writer.write("################################ Json to Object ###################################\n");
            writer.write(cv.validateJsonToObject(json) + "\n\n\n");

            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find the specified path: " + resultPath);
        } catch (IOException e) {
            System.err.println("Something wrong happened while writing.");
        } finally {
            JsonParserBenchmark.tearDown();
            System.out.println("### Differential Testing completed. ###");
        }

    }

    public static void main(String[] args) {

        // initialization and parse command line
        buildArgs();
        init();
        parseArgs(args);

        // run measurement
        runDifferentialTesting();
        runMemoryMeasurement();
        runTimeMeasurement();
    }
}
