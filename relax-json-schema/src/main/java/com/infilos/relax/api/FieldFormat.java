package com.infilos.relax.api;

import java.lang.annotation.*;

import com.infilos.relax.field.*;

/**
 * Annotation to annotate a bean's field to represent a column with format information similar to the ´format` property of a Schema entry .
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldFormat {

    /**
     * <pre>
     * Currently valid for:
     *
     * - {@link DateField}
     * - {@link TimeField}
     * - {@link DatetimeField}
     * </pre>
     */
    String value() default "default";
}
