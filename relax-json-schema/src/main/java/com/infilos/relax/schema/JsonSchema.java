package com.infilos.relax.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.infilos.relax.error.ValidationException;
import com.infilos.relax.util.JsonUtil;
import com.infilos.utils.Loggable;
import com.networknt.schema.*;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

public class JsonSchema implements Loggable {
    private final boolean strictValidation;
    private final com.networknt.schema.JsonSchema jsonSchema;

    private JsonSchema(JsonNode schemaNode, boolean strictValidation) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        this.jsonSchema = factory.getSchema(schemaNode);
        this.strictValidation = strictValidation;
    }

    public static JsonSchema fromJson(String jsonSchema) {
        return fromJson(jsonSchema, true);
    }

    public static JsonSchema fromJson(String jsonSchema, boolean strictValidation) {
        return new JsonSchema(JsonUtil.getInstance().readValue(jsonSchema), strictValidation);
    }

    public static JsonSchema fromJson(InputStream jsonSchema, boolean strictValidation) {
        return new JsonSchema(JsonUtil.getInstance().readValue(jsonSchema), strictValidation);
    }

    public Set<ValidationMessage> validate(String json) {
        return validate(JsonUtil.getInstance().readValue(json));
    }

    public Set<ValidationMessage> validate(JsonNode json) {
        Set<ValidationMessage> errors = jsonSchema.validate(json);
        if (errors.isEmpty()) {
            return Collections.emptySet();
        } else {
            String msg = String.format("validation failed: %s", errors);
            if (this.strictValidation) {
                log().warn(msg);
                throw new ValidationException(this, errors);
            } else {
                log().info(msg);
                return errors;
            }
        }
    }
}
