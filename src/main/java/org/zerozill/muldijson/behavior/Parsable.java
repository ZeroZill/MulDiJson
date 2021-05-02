package org.zerozill.muldijson.behavior;

public interface Parsable {
    /**
     * Parse the <code>json</code> string to a JsonObject.
     *
     * @param <T>   The type of library defined json object.
     * @param json  The string to be parsed.
     * @return The library defined json object.
     */
    <T> T parse(String json);
}
