package com.infilos.relax.flat;

import com.infilos.relax.JsonCompactor;
import com.infilos.relax.JsonFlattener;

/**
 * Defines an interface to transform keys in {@link JsonFlattener} or {@link JsonCompactor}.
 */
@FunctionalInterface
public interface KeyTransformer {

    /**
     * Transforms the given key by this function.
     *
     * @param key any JSON key
     * @return the new JSON key
     */
    String transform(String key);
}
