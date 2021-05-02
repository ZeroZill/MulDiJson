package org.zerozill.muldijson.behavior;

public interface Serializable {

    /**
     * Serializes a <code>bean</code> to a JSON string.
     *
     * @param bean The Bean to be serialized.
     * @param <T>  The type of the <code>bean</code>.
     * @return The corresponding JSON string.
     */
    <T> String serialize(T bean);

}
