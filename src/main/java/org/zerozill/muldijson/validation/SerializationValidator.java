package org.zerozill.muldijson.validation;

public interface SerializationValidator {
    /**
     * Checks if <code>parser</code> can serialize <code>object</code> to the same result as
     * <code>targetJson</code>.
     *
     * @param object     Input object, which can be a Bean for a parser whose target is a Bean
     *                   or a JsonObject for a parser whose target is an object defined by itself.
     * @param targetJson Target Json string to be compared with.
     * @param <T>        The type of input object, which can be the type of Bean or a type defined
     *                   by parser library.
     * @return The corresponding <code>ValidationResult</code>.
     * @throws NullPointerException <code>parser</code> cannot be <code>null</code>.
     */
    public <T> ValidationResult isSameSerializationResult(T object, String targetJson) throws NullPointerException;

    /**
     * Checks if <code>parser</code> can serialize <code>object</code> to the same result as
     * <code>targetJson</code>.
     *
     * @param json       The result of serialization.
     * @param targetJson Target Json string to be compared with.
     * @param <T>        The type of input object, which can be the type of Bean or a type defined
     *                   by parser library.
     * @return The corresponding <code>ValidationResult</code>.
     * @throws NullPointerException <code>parser</code> cannot be <code>null</code>.
     */
    public <T> ValidationResult isSameSerializationResult(String json, String targetJson) throws NullPointerException;

}
