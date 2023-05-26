package com.infilos.relax.enumtest;

import com.fasterxml.jackson.annotation.JsonValue;

public interface CodeBasedEnum<E extends Enum<E>> {

    @JsonValue
    int getCode();

    /**
     * Implement json creator for each concret enum:
     * <pre>{@code
     * @JsonCreator
     * public static E fromCode(int code) {
     *     return CodeBasedEnum.fromCode(E.class, code);
     * }
     * }</pre>
     */
    static <T extends CodeBasedEnum<E>, E extends Enum<E>> T fromCode(Class<T> type, int code) {
        for (T t : type.getEnumConstants()) {
            if (t.getCode() == code) {
                return t;
            }
        }

        throw new IllegalArgumentException(String.format("%s cannot match the code: %s", type.getSimpleName(), code));
    }
}

