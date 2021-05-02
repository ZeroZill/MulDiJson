package org.zerozill.muldijson.behavior;

import org.json.JSONObject;

public interface Deserializable {

    /**
     * Deserializes the <code>json</code> string to a Bean.
     *
     * @param json  The string to be deserialized.
     * @param clazz The target class.
     * @param <T>   The type of expected Bean.
     * @return The corresponding Bean.
     */
    <T> T deserialize(String json, Class<T> clazz);
}
