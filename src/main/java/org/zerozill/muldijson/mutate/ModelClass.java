package org.zerozill.muldijson.mutate;

import groovy.lang.GroovyClassLoader;
import org.zerozill.muldijson.Config;
import org.zerozill.muldijson.ConfigModel;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

public class ModelClass {

    private ModelField[] fields;
    private String classSimpleName;

    public ModelClass(Class<?> clazz) {
        this.classSimpleName = clazz.getSimpleName();
        Field[] fs = clazz.getDeclaredFields();
        fields = new ModelField[fs.length];
        for (int i = 0; i < fs.length; i++) {
            fields[i] = new ModelField(fs[i]);
        }
    }

    public ModelClass(ModelField[] fields, String classSimpleName) {
        this.fields = fields;
        this.classSimpleName = classSimpleName;
    }

    public ModelField[] getFields() {
        return fields;
    }

    public void setFields(ModelField[] fields) {
        this.fields = fields;
    }

    public void applyMutations() {
        Random random = new Random();
        Config config = ConfigModel.getConfig();
        for (int i = 0; i < config.mutationTimes; i++) {
            try {
                // Choose the mutation type
                MutationType[] mutationTypes = MutationType.values();
                int index = random.nextInt(mutationTypes.length);
                MutationType mutationType = mutationTypes[index];
                Method mutationMethod = Mutation.mutationsMethodMap.get(mutationType);

                // Choose the mutation field
                index = random.nextInt(fields.length);

                // Mutate
                mutationMethod.invoke(null, this, index);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public Class<?> loadClass() {
        String classString = toClassFile();
        Config config = ConfigModel.getConfig();
        String recordPath = config.outputFileName;

        // record the mutated class
        recordClassFile(recordPath, classString);

        GroovyClassLoader classLoader = new GroovyClassLoader();
        return (Class<?>) classLoader.parseClass(classString);
    }

    public Class<?> mutateAndLoadClass() {
        applyMutations();
        return loadClass();
    }

    public Object getInstance() {
        Object instance = null;
        try {
            Class<?> clazz = mutateAndLoadClass();
            Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
            instance = defaultConstructor.newInstance();
            setInstanceFields(instance);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private void setInstanceFields(Object instance) {
        for (ModelField field : fields) {
            String fieldName = field.getName();
            Object fieldInstance = field.getInstance();
            char[] nameChars = fieldName.toCharArray();
            char firstChar = nameChars[0];
            nameChars[0] = Character.toUpperCase(firstChar);
            fieldName = String.valueOf(nameChars);
            try {
                Method f = instance.getClass().getDeclaredMethod("set" + fieldName, field.getClazz());
                f.invoke(instance, fieldInstance);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static void recordClassFile(String recordPath, String classFile) {
        String resultPath = "result/class/" + recordPath + ".java";
        // write the first line of result
        File clazz = new File(resultPath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(clazz));
            writer.write(classFile);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find the specified path: " + resultPath);
        } catch (IOException e) {
            System.err.println("Something wrong happened while writing.");
        }

    }

    public String toClassFile() {
        if (fields == null) {
            throw new NullPointerException("Not initialized yet.");
        }

        return
                // Package
                getPackageString() +
                        // Import
                        getImportString() +
                        "@JsonObject\n" +
                        "public class Mutated" + classSimpleName + " {\n" +
                        // Definition Part
                        getDefinitionString() +
                        // Constructor
                        getConstructorString() +
                        // Getter and Setter
                        getGetterSetterString() +
                        "}\n";
    }

    private String getPackageString() {
        return "package org.zerozill.muldijson.model;\n\n";
    }

    private String getImportString() {
        return "import java.util.*;\n" +
                "import java.lang.*;\n" +
                "import org.zerozill.muldijson.model.*;\n" +
                "import com.bluelinelabs.logansquare.annotation.JsonField;\n" +
                "import com.bluelinelabs.logansquare.annotation.JsonObject;\n" +
                "\n";
    }

    private String getDefinitionString() {
        StringBuilder sb = new StringBuilder();

        for (ModelField f : fields) {
            sb.append("\t").append(f.getDefinitionString());
        }

        return sb.append("\n").toString();
    }

    private String getConstructorString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\t").append("public Mutated").append(classSimpleName).append("() {\n")
                .append("\n")
                .append("\t").append("}\n\n");

        sb.append("\t").append("public Mutated").append(classSimpleName).append("(");

        for (int i = 0; i < fields.length; i++) {
            ModelField f = fields[i];
            sb.append(f.getTypeString()).append(f.getParametersString()).append(" ").append(f.getName());
            if (i != fields.length - 1) {
                sb.append(",");
            }
        }
        sb.append(") {\n");

        for (ModelField f : fields) {
            sb.append("\t\t").append(f.getAssignmentString());
        }

        sb.append("\t").append("}\n\n");

        return sb.toString();
    }

    private String getGetterSetterString() {
        StringBuilder sb = new StringBuilder();

        for (ModelField f : fields) {
            sb.append(f.getGetterAndSetter());
        }

        return sb.toString();
    }
}
