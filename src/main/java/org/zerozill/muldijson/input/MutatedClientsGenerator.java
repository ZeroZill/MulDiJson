package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Carriage;
import org.zerozill.muldijson.model.Clients;
import org.zerozill.muldijson.mutate.ModelClass;

public class MutatedClientsGenerator extends InputGenerator {

    private Object instance;

    public MutatedClientsGenerator() {
        ModelClass modelClass = new ModelClass(Clients.class);
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
