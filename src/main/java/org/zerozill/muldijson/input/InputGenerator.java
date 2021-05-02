package org.zerozill.muldijson.input;

public abstract class InputGenerator {

    public InputGenerator() {

    }

    @Override
    public String toString() {
        return "Input";
    }

    public abstract String generateJson();

    public abstract Object generateBean();

}
