package org.zerozill.muldijson.mutate;

import org.apache.commons.lang3.ArrayUtils;
import org.zerozill.muldijson.model.Carriage;
import org.zerozill.muldijson.model.Users;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Mutation {

    static int classNo = 0;

    static boolean hasInit = false;
    static ArrayList<Class<?>> allowLoganClasses;
    static ArrayList<Class<?>> classes;
    static ArrayList<Class<?>> parameters;
    static float arrayRate = 0.05f;
    static float underLineRate = 0.1f;

    public static Map<MutationType, Method> mutationsMethodMap
            = new HashMap<MutationType, Method>() {
        {
            try {
                put(MutationType.Type, Mutation.class.getDeclaredMethod("changeType", ModelClass.class, int.class));
                put(MutationType.Name, Mutation.class.getDeclaredMethod("changeName", ModelClass.class, int.class));
                put(MutationType.Modifier, Mutation.class.getDeclaredMethod("changeModifier", ModelClass.class, int.class));
                put(MutationType.Parameter, Mutation.class.getDeclaredMethod("changeParameterType", ModelClass.class, int.class));
                put(MutationType.Delete, Mutation.class.getDeclaredMethod("deleteField", ModelClass.class, int.class));
                put(MutationType.Add, Mutation.class.getDeclaredMethod("addField", ModelClass.class, int.class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    };

    static Random random = new Random();

    private static void init() {
        if (hasInit) {
            return;
        }

        parameters = new ArrayList<>();

        // objects of basic type
        parameters.add(Short.class);
        parameters.add(Integer.class);
        parameters.add(Long.class);
        parameters.add(Float.class);
        parameters.add(Double.class);
        parameters.add(Boolean.class);
        parameters.add(Character.class);
        parameters.add(Byte.class);

        // models
        parameters.add(Carriage.class);
        parameters.add(Users.class);

        allowLoganClasses = new ArrayList<>(parameters);
        // collections
        allowLoganClasses.add(Set.class);
        allowLoganClasses.add(List.class);
        allowLoganClasses.add(Map.class);

        // basic type (supported by logan square)
        allowLoganClasses.add(int.class);
        allowLoganClasses.add(long.class);
        allowLoganClasses.add(float.class);
        allowLoganClasses.add(double.class);
        allowLoganClasses.add(boolean.class);
        allowLoganClasses.add(byte.class);

        classes = new ArrayList<>(allowLoganClasses);
        // basic type (not supported by logan square)
        classes.add(short.class);
        classes.add(char.class);

        hasInit = true;
    }

    private static String[] generateParameters(Class<?> type) {
        String[] paras = null;
        if (type.isAssignableFrom(Set.class) || type.isAssignableFrom(List.class)) {
            paras = new String[1];
            int index = random.nextInt(parameters.size());
            paras[0] = parameters.get(index).getCanonicalName();
        } else if (type.isAssignableFrom(Map.class)) {
            paras = new String[2];
            int index1 = random.nextInt(parameters.size());
            paras[0] = parameters.get(index1).getCanonicalName();
            int index2 = random.nextInt(parameters.size());
            paras[1] = parameters.get(index2).getCanonicalName();
        }
        return paras;
    }

    private static String generateName() {
        int len = random.nextInt(20) + 4;
        char[] str = new char[len];
        str[0] = (char) (97 + random.nextInt(26));
        for (int i = 1; i < len; i++) {
            if (random.nextFloat() > underLineRate) {
                boolean upperCase = random.nextBoolean();
                str[i] = (char) (97 + random.nextInt(26));
                if (upperCase) {
                    str[i] = Character.toUpperCase(str[i]);
                }
            } else {
                str[i] = '_';
            }
        }

        return new String(str);
    }

    public static void changeType(ModelClass modelClass, int index, boolean allowLogan) {
        if (!hasInit) {
            init();
        }

        ArrayList<Class<?>> types = allowLogan ? allowLoganClasses : classes;
        int typesCount = types.size();
        int typeIndex = random.nextInt(typesCount);

        ModelField modelField = modelClass.getFields()[index];

        Class<?> type = types.get(typeIndex);
        ModelField.Modifier modifier = modelField.getModifier();
        String typeString = type.getCanonicalName();

        if (!(type.isAssignableFrom(Set.class) || type.isAssignableFrom(List.class) || type.isAssignableFrom(Map.class))) {
//        while (random.nextFloat() < arrayRate) {
//            typeString = typeString + "[]";
//        }
            //todo
        }

        String name = modelField.getName();
        String[] paras = generateParameters(type);

        Field f = getAField(modifier, type, typeString, paras, name);
        modelClass.getFields()[index] = new ModelField(f);
    }

    public static void changeType(ModelClass modelClass, int index) {
        changeType(modelClass, index, true);
    }

    public static void changeName(ModelClass modelClass, int index) {
        if (!hasInit) {
            init();
        }

        modelClass.getFields()[index].setName(generateName());
    }

    public static void changeModifier(ModelClass modelClass, int index) {
        if (!hasInit) {
            init();
        }
        ModelField.Modifier[] modifiers = ModelField.Modifier.values();
        int i = random.nextInt(modifiers.length);
        modelClass.getFields()[index].setModifier(modifiers[i]);
    }

    public static void changeParameterType(ModelClass modelClass, int index) {
        if (!hasInit) {
            init();
        }

        ModelField modelField = modelClass.getFields()[index];
        Class<?> type = modelField.getClazz();
        if (!(type.isAssignableFrom(Set.class) || type.isAssignableFrom(List.class) || type.isAssignableFrom(Map.class))) {
            return;
        }

        ModelField.Modifier modifier = modelField.getModifier();
        String typeString = type.getCanonicalName();
        String name = modelField.getName();
        String[] paras = generateParameters(type);

        Field f = getAField(modifier, type, typeString, paras, name);
        modelClass.getFields()[index] = new ModelField(f);
    }

    public static void deleteField(ModelClass modelClass, int index) {
        if (!hasInit) {
            init();
        }

        ModelField[] fields = modelClass.getFields();
        if (fields.length > 1) {
            fields = ArrayUtils.remove(fields, index);
            modelClass.setFields(fields);
        }
    }

    public static void addField(ModelClass modelClass, int index, boolean allowLogan) {
        if (!hasInit) {
            init();
        }

        ModelField.Modifier[] values = ModelField.Modifier.values();
        ModelField.Modifier modifier = values[random.nextInt(values.length)];

        ArrayList<Class<?>> types = allowLogan ? allowLoganClasses : classes;
        int typeCount = types.size();
        int typeIndex = random.nextInt(typeCount);
        Class<?> type = types.get(typeIndex);

        String typeString = type.getCanonicalName();

        String[] paras = generateParameters(type);

        String name = generateName();

        Field f = getAField(modifier, type, typeString, paras, name);
        ModelField newModelField = new ModelField(f);

        ModelField[] fields = modelClass.getFields();
        fields = ArrayUtils.add(fields, newModelField);
        modelClass.setFields(fields);
    }

    public static void addField(ModelClass modelClass, int index) {
        addField(modelClass, index, true);
    }


    private static Field getAField(ModelField.Modifier modifier, Class<?> clazz, String typeString, String[] parameters, String name) {
        Field f = null;
        try {
            ModelField newField = new ModelField(modifier, clazz, typeString, parameters, name);
            ModelField[] fields = new ModelField[]{newField};
            ModelClass tempModelClass = new ModelClass(fields, "TempClass" + classNo);
            classNo++;
            Class<?> tempClass = tempModelClass.loadClass();

            f = tempClass.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return f;
    }
}

enum MutationType {
    Type,
    Name,
    Modifier,
    Parameter,
    Delete,
    Add
}