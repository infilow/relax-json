package com.infilos.relax.error;

import com.infilos.relax.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ValidationException extends TableSchemaException {

    List<ValidationMessage> validationMessages = new ArrayList<>();

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(JsonSchema schema, Collection<ValidationMessage> messages) {
        this(String.format("%s: %s, %s", schema, "validation failed", StringUtils.join(messages, ", ")));
        this.validationMessages.addAll(messages);
    }
}
