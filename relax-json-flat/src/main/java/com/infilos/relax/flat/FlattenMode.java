package com.infilos.relax.flat;

public enum FlattenMode {
    /**
     * Flattens every objects.
     */
    NORMAL,
    /**
     * Conforms to MongoDB dot.notation to update also nested documents.
     */
    MONGO,
    /**
     * Flattens every objects except arrays.
     */
    KEEP_ARRAYS,
    /**
     * Flattens every objects except arrays which contain only primitive types(strings, numbers, booleans, and null).
     */
    KEEP_PRIMITIVE_ARRAYS
}
