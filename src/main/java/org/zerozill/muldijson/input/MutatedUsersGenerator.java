package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Users;
import org.zerozill.muldijson.mutate.ModelClass;

public class MutatedUsersGenerator extends InputGenerator {

    private final Object instance;

    public MutatedUsersGenerator() {
        ModelClass modelClass = new ModelClass(Users.class);
        modelClass.applyMutations();
        instance = modelClass.getInstance();
    }

    @Override
    public String generateJson() {
        return new Gson().toJson(instance);
    }

    @Override
    public Object generateBean() {
        return instance;
    }
}
