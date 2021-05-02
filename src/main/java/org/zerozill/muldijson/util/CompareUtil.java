package org.zerozill.muldijson.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Set;

public class CompareUtil {

    private static StringBuilder extraKVBuilder1;
    private static StringBuilder extraKVBuilder2;
    private static StringBuilder diffKVBuilder;
    public static String separateLine1 = "**************************************************************************\n";
    public static String separateLine2 = "=====================================================================\n";

    private static void initBuilders() {
        extraKVBuilder1 = new StringBuilder()
                .append(separateLine2)
                .append("Extra JSON elements:\n")
                .append(separateLine2);
        extraKVBuilder2 = new StringBuilder()
                .append(separateLine2)
                .append("Missing JSON elements:\n")
                .append(separateLine2);
        diffKVBuilder = new StringBuilder()
                .append(separateLine2)
                .append("Different JSON elements:\n")
                .append(separateLine2);
    }

    public static String getDifferences(JsonObject object, JsonObject targetObject) {
        if (object.equals(targetObject)) {
            return null;
        }

        Set<String> keySet = object.keySet();
        Set<String> targetKeySet = targetObject.keySet();

        initBuilders();

        for (String key : keySet) {
            if (!targetKeySet.contains(key)) {
                extraKVBuilder1.append(String.format("\"%s\" : %s\n\n", key, object.get(key)));
            } else {
                JsonElement element1 = object.get(key);
                JsonElement element2 = targetObject.get(key);
                if (!element1.equals(element2)) {
                    diffKVBuilder.append(
                            String.format(
                                    "Different key : \"%s\"\n" +
                                            "JSON element :\t\t\t%s\n" +
                                            "Target JSON element :\t%s\n\n",
                                    key, element1, element2));
                }
            }
        }

        for (String key : targetKeySet) {
            if (!keySet.contains(key)) {
                extraKVBuilder2.append(String.format("\"%s\" : %s\n\n", key, targetObject.get(key)));
            }
        }

        return extraKVBuilder1.
                append(extraKVBuilder2).
                append(diffKVBuilder)
                .toString();
    }
}
