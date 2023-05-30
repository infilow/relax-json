package com.infilos.relax.error;

public class TableValidationException extends TableSchemaException {

    public TableValidationException(String message) {
        super(message);
    }

    public TableValidationException(Throwable cause) {
        super(cause);
    }
}