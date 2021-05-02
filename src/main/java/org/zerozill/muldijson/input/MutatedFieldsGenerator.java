package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Fields;
import org.zerozill.muldijson.mutate.ModelClass;

public class MutatedFieldsGenerator extends InputGenerator{
    private Object instance;

    public MutatedFieldsGenerator() {
        ModelClass modelClass = new ModelClass(Fields.class);
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
