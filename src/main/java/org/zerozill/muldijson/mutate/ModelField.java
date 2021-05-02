package org.zerozill.muldijson.mutate;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.zerozill.muldijson.input.CarriageGenerator;
import org.zerozill.muldijson.input.InputGenerator;
import org.zerozill.muldijson.input.UsersGenerator;
import org.zerozill.muldijson.model.Carriage;
import org.zerozill.muldijson.model.Users;

import java.lang.reflect.*;
import java.util.*;

public class ModelField {

    private final Random random = new Random();
    private Modifier modifier;
    private Class<?> clazz;
    private String name;
    private String typeString;
    private Type type;
    private String[] parameters;

    public ModelField(Field field) {
        name = field.getName();
        type = field.getGenericType();
        typeString = field.getType().getTypeName();
        clazz = field.getType();

        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] actualTypes = pType.getActualTypeArguments();
            parameters = new String[actualTypes.length];
            for (int i = 0; i < actualTypes.length; i++) {
                parameters[i] = actualTypes[i].toString().replaceFirst("class ", "");
            }
        }

        int mod = field.getModifiers();
        // ignore first 29 bits, only care about default, public, private, protected
        mod = mod << 29 >> 29;
        if (mod == 0) {
            modifier = Modifier.DEFAULT;
        } else if ((mod & 0x00000001) == 1) {
            modifier = Modifier.PUBLIC;
        } else if ((mod & 0x00000002) == 2) {
            modifier = Modifier.PRIVATE;
        } else if ((mod & 0x00000004) == 4) {
            modifier = Modifier.PROTECTED;
        }

    }

    public ModelField(Modifier modifier, Class<?> clazz, String typeString, String[] parameters, String name) {
        this.clazz = clazz;
        this.modifier = modifier;
        this.typeString = typeString;
        this.name = name;
        this.parameters = parameters;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public String[] getParameters() {
        return parameters;
    }

    public String getDefinitionString() {

        StringBuilder sb = new StringBuilder();
        // annotation
        sb.append("@JsonField\n").append("\t");
        // modifier
        switch (modifier) {
            case PRIVATE:
                sb.append("private ");
                break;
            case PROTECTED:
                sb.append("protected ");
                break;
            case PUBLIC:
                sb.append("public ");
                break;
            default:
                break;
        }
        // class
        sb.append(getTypeString());
        // parameters
        sb.append(getParametersString());
        // name
        sb.append(" ").append(name).append(";\n\n");
        return sb.toString();
    }

    public String getAssignmentString() {
        return "this." + name + " = " + name + ";\n";
    }

    public String getGetterAndSetter() {
        StringBuilder stringBuilder = new StringBuilder();
        String firstCharInName = Character.toString(name.charAt(0));
        String firstCharInNameInCapital = Character.toString(Character.toUpperCase(name.charAt(0)));
        String upperCaseName = name.replaceFirst(firstCharInName, firstCharInNameInCapital);

        // getter
        stringBuilder.append("\t").append("public ")
                .append(getTypeString())
                .append(getParametersString())
                .append(" get")
                .append(upperCaseName)
                .append("() {\n")
                .append("\t\t")
                .append("return this.")
                .append(name)
                .append(";\n")
                .append("\t}\n");

        stringBuilder.append("\n");

        //setter
        stringBuilder.append("\t").append("public void set")
                .append(upperCaseName)
                .append("(")
                .append(getTypeString())
                .append(getParametersString())
                .append(" ")
                .append(name)
                .append(") {\n")
                .append("\t\tthis.")
                .append(name)
                .append(" = ")
                .append(name)
                .append(";\n")
                .append("\t}\n");

        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public String getParametersString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (parameters != null && parameters.length > 0) {
            stringBuilder.append("<");
            for (int i = 0; i < parameters.length; i++) {
                stringBuilder.append(parameters[i]);
                if (i < parameters.length - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(">");
        }
        return stringBuilder.toString();
    }

    public Object getInstance() {
        return getInstance(clazz);
    }

    private Object getInstance(Class<?> clazz) {
        if (type == null) {
            throw new RuntimeException("Initialize type before generating instance.");
        }

        if (clazz == short.class || clazz == Short.class) {
            return (short) (random.nextInt(0xffff) - 0x8000);
        } else if (clazz == int.class || clazz == Integer.class) {
            return (random.nextInt());
        } else if (clazz == long.class || clazz == Long.class) {
            return random.nextLong();
        } else if (clazz == float.class || clazz == Float.class) {
            return random.nextFloat() * (float) (random.nextInt(0xffff) - 0x8000);
        } else if (clazz == double.class || clazz == Double.class) {
            return random.nextDouble() * (double) (random.nextInt(0xffff) - 0x8000);
        } else if (clazz == boolean.class || clazz == Boolean.class) {
            return random.nextBoolean();
        } else if (clazz == byte.class || clazz == Byte.class) {
            byte[] b = new byte[1];
            random.nextBytes(b);
            return b[0];
        } else if (clazz == char.class || clazz == Character.class) {
            return (char) (random.nextInt(0x10000));
        } else if (clazz == String.class) {
            int len = random.nextInt(20) + 4;
            char[] str = new char[len];
            str[0] = 's';
            str[1] = 't';
            str[2] = 'r';
            for (int i = 3; i < len; i++) {
                str[i] = (char) (random.nextInt(0x10000));
            }
            return new String(str);
        } else {
            int dimensions = 0;
            Class<?> cl = clazz;
            if (clazz.isArray()) {
                do {
                    dimensions++;
                    cl = cl.getComponentType();
                } while (cl.isArray());
            }

            if (dimensions > 0) {
                int[] dims = new int[dimensions];
                for (int i = 0; i < dimensions; i++) {
                    dims[i] = random.nextInt(5) + 1;
                }
                Object obj = Array.newInstance(cl, dims);
                setArrayInstance(obj, 0, dims, cl);
                return obj;
            } else {
                return getNonPrimitiveInstance(cl);
            }
        }
    }

    private void setArrayInstance(Object obj, int dimIndex, int[] dims, Class<?> clazz) {
        if (dimIndex >= dims.length) {
            return;
        }

        for (int i = 0; i < dims[dimIndex]; i++) {
            if (dimIndex < dims.length - 1) {
                Object subObj = Array.get(obj, i);
                setArrayInstance(subObj, dimIndex + 1, dims, clazz);
            } else {
                Array.set(obj, i, getInstance(clazz));
            }
        }
    }

    private Object getNonPrimitiveInstance(Class<?> clazz) {
        if (clazz.isAssignableFrom(List.class)) {
            ArrayList list = null;
            if (type instanceof ParameterizedType) {
                try {
                    ParameterizedType pType = (ParameterizedType) type;
                    Type[] actualTypes = pType.getActualTypeArguments();
                    assert actualTypes.length == 1;
                    list = ArrayList.class.getDeclaredConstructor().newInstance();
                    int size = random.nextInt(10) + 1;
                    for (int i = 0; i < size; i++) {
                        list.add(getInstance((Class<?>) actualTypes[0]));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return list;
        } else if (clazz.isAssignableFrom(Set.class)) {
            HashSet set = null;
            if (type instanceof ParameterizedType) {
                try {
                    ParameterizedType pType = (ParameterizedType) type;
                    Type[] actualTypes = pType.getActualTypeArguments();
                    assert actualTypes.length == 1;
                    set = HashSet.class.getDeclaredConstructor().newInstance();
                    int size = random.nextInt(10) + 1;
                    for (int i = 0; i < size; i++) {
                        set.add(getInstance((Class<?>) actualTypes[0]));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return set;
        } else if (clazz.isAssignableFrom(Map.class)) {
            HashMap map = null;
            if (type instanceof ParameterizedType) {
                try {
                    ParameterizedType pType = (ParameterizedType) type;
                    Type[] actualTypes = pType.getActualTypeArguments();
                    assert actualTypes.length == 2;
                    map = HashMap.class.getDeclaredConstructor().newInstance();
                    int size = random.nextInt(10) + 1;
                    for (int i = 0; i < size; i++) {
                        Object key = getInstance((Class<?>) actualTypes[0]);
                        Object value = getInstance((Class<?>) actualTypes[1]);
                        map.put(key, value);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return map;
        } else {
            Object obj = getModelInstance(clazz);
            if (obj == null) {
                // obj is not a model class
                try {
                    obj = clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            return obj;
        }
    }

    private Object getModelInstance(Class<?> clazz) {
        InputGenerator ig;
        if (clazz == Carriage.class) {
            ig = new CarriageGenerator();
            return ig.generateBean();
        } else if (clazz == Users.class) {
            ig = new UsersGenerator();
            return ig.generateBean();
        }

        return null;
    }

    public enum Modifier {
        DEFAULT,
        PRIVATE,
        PROTECTED,
        PUBLIC
    }
}
