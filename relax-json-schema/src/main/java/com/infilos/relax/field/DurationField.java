package com.infilos.relax.field;

import com.infilos.relax.error.*;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

public class DurationField extends Field<Duration> {

    DurationField() {
        super();
    }

    public DurationField(String name) {
        super(name, FIELD_TYPE_DURATION);
    }

    public DurationField(String name, String format, String title, String description,
                         URI rdfType, Map<String, Object> constraints,
                         Map<String, Object> options){
        super(name, FIELD_TYPE_DURATION, format, title, description, rdfType, constraints, options);
    }

    @Override
    public Duration parseValue(String value, String format, Map<String, Object> options)
            throws InvalidCastException, ConstraintsException {
        try{
            return Duration.parse(value);
        }catch(Exception e){
            throw new TypeInferringException(e);
        }
    }

    @Override
    public String formatValueAsString(Duration value, String format, Map<String, Object> options) throws InvalidCastException, ConstraintsException {
        return value.toString();
    }


    @Override
    public String parseFormat(String value, Map<String, Object> options) {
        return "default";
    }
}
