package org.zerozill.muldijson.validation;

public interface DeserializationValidator {
    /**
     * Checks if <code>parser</code> can deserialize <code>json</code> to the same result as
     * <code>targetObject</code> (Bean or library defined targetObject).
     *
     * @param json         Input Json string.
     * @param targetObject Target targetObject to be compared with (Bean or library defined targetObject).
     * @param <T>          The type of input targetObject, which can be the type of Bean or a type defined
     *                     by parser library.
     * @return The corresponding <code>ValidationResult</code>.
     * @throws NullPointerException <code>parser</code> cannot be <code>null</code>.
     */
    public abstract <T> ValidationResult isSameDeserializationResult(String json, T targetObject) throws NullPointerException;

    /**
     * Checks if <code>parser</code> can deserialize <code>json</code> to the same result as
     * <code>targetObject</code> (Bean or library defined targetObject).
     *
     * @param object       Input Bean or library defined Json object.
     * @param targetObject Target targetObject to be compared with (Bean or library defined targetObject).
     * @param <E>          The type of input targetObject, which can be the type of Bean or a type defined
     *                     by parser library.
     * @param <T>          The type of input targetObject, which can be the type of Bean or a type defined
     *                     by parser library.
     * @return The corresponding <code>ValidationResult</code>.
     * @throws NullPointerException <code>parser</code> cannot be <code>null</code>.
     */
    public abstract <E, T> ValidationResult isSameDeserializationResult(E object, T targetObject) throws NullPointerException;
}
