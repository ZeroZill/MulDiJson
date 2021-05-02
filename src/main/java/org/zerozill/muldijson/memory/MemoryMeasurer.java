package org.zerozill.muldijson.memory;

import com.bluelinelabs.logansquare.NoSuchMapperException;
import org.zerozill.muldijson.JsonParserBenchmark;

import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryMeasurer {

    private List<Method> includes;
    private String resultPath;
    private int forks;
    private int iterations;


    public MemoryMeasurer() {
        includes = Arrays.stream(JsonParserBenchmark.class.getDeclaredMethods()).filter(m -> m.getName().contains("test")).collect(Collectors.toList());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        resultPath = "result/memory_" + simpleDateFormat.format(new Date()) + ".csv";
        forks = 5;
        iterations = 1;
    }

    public void setIncludes(List<Method> includes) {
        this.includes = includes;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public void setResultPath(String resultPath) {
        if (resultPath == null || resultPath.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            this.resultPath = "result/memory_" + simpleDateFormat.format(new Date()) + ".csv";
        }
        this.resultPath = resultPath;
    }

    public void run() {
        // write the first line of result
        File csv = new File(resultPath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(csv, true));
            writer.write("Target,Input,Fork,Memory Consumption / KB\n");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find the specified path: " + resultPath);
        } catch (IOException e) {
            System.err.println("Something wrong happened while writing.");
        }

        for (Method m : includes) {
            for (int i = 0; i < forks; i++) {
                exec(m, i+1, iterations);
            }
        }

    }

    private void exec(Method m, int forkNo, int iterations) {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = MeasurementExecutor.class.getName();
        List<String> args = Arrays.asList(new String[]{m.getName(), Integer.toString(forkNo), Integer.toString(iterations), resultPath}.clone());

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        command.addAll(args);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process p = processBuilder.inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMapperException e) {
            System.err.println("[Memory Measurement]Logan Square does not support runtime loaded class, here we skip it.");
        }
    }
}
