package org.zerozill.muldijson.behavior;

public interface Writable {

    /**
     * Write a library defined <code>jsonObject</code> to a JSON string.
     *
     * @param jsonObject The library defined jsonObject.
     * @param <T>        The type of the <code>jsonObject</code>.
     * @return The corresponding JSON string.
     */
    <T> String write(T jsonObject);

}
