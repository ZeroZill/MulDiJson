package org.zerozill.muldijson;

import org.zerozill.muldijson.parser.Parsers;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigModel {
    private volatile static Config config;

    private ConfigModel() {
    }

    public static Config getConfig() {
        if (config == null) {
            synchronized (Config.class) {
                if (config == null) {
                    config = new Config();
                }
            }
        }
        return config;
    }

    /**
     * Gets the corresponding input mode based on the string parameter <code>mode</code>.
     * If <code>mode</code> is null, empty or other string that is not a name of any input mode,
     * this method will return the default input mode, <code>InputMode.CARRIAGE</code>.
     *
     * @param mode The name of corresponding input mode.
     * @return The corresponding input mode.
     */
    public static InputMode getInputMode(String mode) {
        if (mode == null || mode.equals("")) {
            return InputMode.CARRIAGE;
        }

        mode = mode.trim();
        for (InputMode im : InputMode.values()) {
            if (mode.equalsIgnoreCase(im.toString())) {
                return im;
            }
        }

        return InputMode.CARRIAGE;
    }

    private static String getMethodName(String parserName, String behavior) {
        StringBuilder methodNameBuilder = new StringBuilder();

        methodNameBuilder.append("test").append('_');

        boolean isParser = false;

        for (Parsers p : Parsers.values()) {
            if (parserName.equalsIgnoreCase(p.name())) {
                isParser = true;
                methodNameBuilder.append(p.name());
            }
        }

        if (!isParser) {
            throw new IllegalArgumentException("Undefined parser name.");
        }

        methodNameBuilder.append('_');

        if (behavior.equalsIgnoreCase("serialize")) {
            methodNameBuilder.append("SERIALIZE");
        } else if (behavior.equalsIgnoreCase("deserialize")) {
            methodNameBuilder.append("DESERIALIZE");
        } else if (behavior.equalsIgnoreCase("parse")) {
            methodNameBuilder.append("PARSE");
        } else if (behavior.equalsIgnoreCase("write")) {
            methodNameBuilder.append("WRITE");
        } else {
            throw new IllegalArgumentException("Undefined parser behavior.");
        }

        return methodNameBuilder.toString();
    }

    /**
     * Gets a list of methods to be test from a csv file.
     * The format of this csv file is like:
     * [parser name1],[behavior1]\n
     * [parser name1],[behavior2]\n
     * [parser name2],[behavior1]\n
     * ...
     * <p>
     * If the csv path is null or empty, this method will
     * returns a list that contains all the testing methods.
     *
     * @param includesPath The path of the csv file.
     * @return A list contains the methods to be tested.
     */
    public static List<Method> getIncludes(String includesPath) {
        if (includesPath == null || includesPath.equals("")) {
            return Arrays.stream(JsonParserBenchmark.class.getDeclaredMethods()).filter(m -> m.getName().contains("test")).collect(Collectors.toList());
        }

        File csv = new File(includesPath);
        List<Method> methods = new LinkedList<>();
        String methodName = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csv));
            String line;

            String parserName;
            String behavior;

            while ((line = reader.readLine()) != null) {
                String[] args = line.trim().split(",");

                if (args.length != 2) {
                    throw new IllegalArgumentException("There must be only 2 arguments in one line: <Parser, Behavior>.");
                }

                parserName = args[0].trim();
                behavior = args[1].trim();

                methodName = getMethodName(parserName, behavior);
                methods.add(JsonParserBenchmark.class.getDeclaredMethod(methodName));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find the specified path: " + includesPath);
        } catch (IOException e) {
            System.err.println("Something wrong happened while reading.");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (NoSuchMethodException e) {
            System.err.println("Cannot find method: " + methodName);
        }
        return methods;
    }

    public static boolean isMutatedMode() {
        String modeString = getConfig().inputMode.toString();
        return modeString.contains("MUTATED");
    }
}


enum InputMode {
    CARRIAGE,
    USERS,
    CLIENTS,
    FIELDS,
    MUTATED_CARRIAGE,
    MUTATED_USERS,
    MUTATED_CLIENTS,
    MUTATED_FIELDS
}

