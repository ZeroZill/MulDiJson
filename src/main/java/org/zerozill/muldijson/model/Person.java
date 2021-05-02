package org.zerozill.muldijson.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Person {

    @JsonField
    public String name;

    @JsonField
    public String phone;

    @JsonField
    public int age;

    // Genson needs a non-parameter constructor
    public Person() {

    }

    public Person(String name, String phone, int age) {
        this.name = name;
        this.phone = phone;
        this.age = age;
    }

    public String toString() {
        return "name : " + name + "\n" +
                "phone : " + phone + "\n" +
                "age : " + age + "\n";
    }
}
