package com.infilos.relax.json;

/**
 * @author infilos on 2020-06-13.
 */

public class JsonException extends RuntimeException {

    private JsonException(String message) {
        super(message);
    }

    private JsonException(Throwable cause) {
        super(cause);
    }

    private JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public static JsonException of(String message) {
        return new JsonException(message);
    }

    public static JsonException of(Throwable cause) {
        return new JsonException(cause);
    }

    public static JsonException of(String message, Throwable cause) {
        return new JsonException(message, cause);
    }

    public static JsonException ofAction(String action, Throwable cause) {
        return new JsonException(String.format("%s, %s", action, cause.getMessage()), cause);
    }
}
