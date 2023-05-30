package com.infilos.relax.field;

import com.infilos.relax.error.ConstraintsException;
import com.infilos.relax.error.InvalidCastException;
import com.infilos.relax.util.JsonUtil;

import java.net.URI;
import java.util.Map;

public class ArrayField extends Field<Object[]> {

    ArrayField() {
        super();
    }

    public ArrayField(String name) {
        super(name, FIELD_TYPE_ARRAY);
    }

    public ArrayField(String name, String format, String title, String description,
                      URI rdfType, Map<String, Object> constraints,
                      Map<String, Object> options){
        super(name, FIELD_TYPE_ARRAY, format, title, description, rdfType, constraints, options);
    }

    @Override
    public Object[] parseValue(String value, String format, Map<String, Object> options)
            throws InvalidCastException, ConstraintsException {
        return JsonUtil.getInstance().deserialize(value, Object[].class);
    }

    @Override
    public String formatValueAsString(Object[] value, String format, Map<String, Object> options) throws InvalidCastException, ConstraintsException {
        //return value.toString();
        return JsonUtil.getInstance().serialize(value);
    }

    @Override
    public String parseFormat(String value, Map<String, Object> options) {
        return "default";
    }
}
