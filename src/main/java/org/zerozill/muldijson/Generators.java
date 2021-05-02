package org.zerozill.muldijson;

import org.zerozill.muldijson.input.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Generators {
    // The map records all InputGenerator maps to InputMode.
    public static Map<InputMode, Class<? extends InputGenerator>> inputGeneratorMap
            = new HashMap<InputMode, Class<? extends InputGenerator>>()
    {
        {
            put(InputMode.CARRIAGE, CarriageGenerator.class);
            put(InputMode.USERS, UsersGenerator.class);
            put(InputMode.CLIENTS, ClientsGenerator.class);
            put(InputMode.FIELDS, FieldsGenerator.class);
            put(InputMode.MUTATED_CARRIAGE, MutatedCarriageGenerator.class);
            put(InputMode.MUTATED_USERS, MutatedUsersGenerator.class);
            put(InputMode.MUTATED_CLIENTS, MutatedClientsGenerator.class);
            put(InputMode.MUTATED_FIELDS, MutatedFieldsGenerator.class);
        }
    };

    public static InputGenerator getInputGenerator(InputMode mode) {
        InputGenerator res = null;
        try {
            Constructor<? extends InputGenerator> constructor = inputGeneratorMap.get(mode).getDeclaredConstructor();
            res = constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return res;
    }
}
