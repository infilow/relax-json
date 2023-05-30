package com.infilos.relax.error;

public class TableSchemaException extends RuntimeException {

    public TableSchemaException() {
    }

    public TableSchemaException(String message) {
        super(message);
    }

    public TableSchemaException(Throwable cause) {
        super(cause);
    }
}