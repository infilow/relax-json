package com.infilos.relax.error;

public class ForeignKeyException extends TableSchemaException {

    public ForeignKeyException(String message) {
        super(message);
    }
}
