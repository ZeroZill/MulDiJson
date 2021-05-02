package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Carriage;
import org.zerozill.muldijson.mutate.ModelClass;

public class MutatedCarriageGenerator extends InputGenerator {

    private final Object instance;

    public MutatedCarriageGenerator() {
        ModelClass modelClass = new ModelClass(Carriage.class);
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
