package com.infilos.relax.field;

import com.infilos.relax.error.ConstraintsException;
import com.infilos.relax.error.InvalidCastException;

import java.math.BigInteger;
import java.net.URI;
import java.util.Map;

/**
 * [According to spec](http://frictionlessdata.io/specs/table-schema/index.html#number), a number field
 * consists of "a non-empty finite-length sequence of decimal digits".
 */
public class IntegerField extends Field<BigInteger> {

    IntegerField() {
        super();
    }

    public IntegerField(String name) {
        super(name, FIELD_TYPE_INTEGER);
    }

    public IntegerField(String name, String format, String title, String description,
                        URI rdfType, Map<String, Object> constraints,
                        Map<String, Object> options) {
        super(name, FIELD_TYPE_INTEGER, format, title, description, rdfType, constraints, options);
    }

    @Override
    public BigInteger parseValue(String value, String format, Map<String, Object> options) throws InvalidCastException, ConstraintsException {
        return new BigInteger(value.trim());
    }

    @Override
    public String formatValueAsString(BigInteger value, String format, Map<String, Object> options) throws InvalidCastException, ConstraintsException {
        return value.toString();
    }


    @Override
    public String parseFormat(String value, Map<String, Object> options) {
        return "default";
    }
}
