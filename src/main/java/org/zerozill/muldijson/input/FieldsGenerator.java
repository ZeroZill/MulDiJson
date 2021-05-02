package org.zerozill.muldijson.input;

import com.google.gson.Gson;
import org.zerozill.muldijson.model.Fields;

import java.util.Random;

public class FieldsGenerator extends InputGenerator{

    private Fields fields;

    public FieldsGenerator() {
        Random random = new Random();
        fields = new Fields(random.nextInt(), random.nextInt(), random.nextInt(), random.nextInt());
    }

    @Override
    public String generateJson() {
        return new Gson().toJson(fields);
    }

    @Override
    public Object generateBean() {
        return fields;
    }
}
