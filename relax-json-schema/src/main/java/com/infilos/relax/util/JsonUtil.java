package com.infilos.relax.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.infilos.relax.error.JsonParsingException;
import com.infilos.relax.error.JsonSerializingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class JsonUtil {
    private static JsonUtil instance;
    private boolean indent = true;
    private final ObjectMapper mapper;

    private JsonUtil() {
        this.mapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
            .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .findAndAddModules()
            .build();
    }

    public static JsonUtil getInstance() {
        if (Objects.isNull(instance)) {
            instance = new JsonUtil();
        }
        return instance;
    }

    public ObjectNode createNode() {
        return mapper.createObjectNode();
    }

    public TextNode createTextNode(String value) {
        return new TextNode(value);
    }

    public JsonNode createNode(String content) {
        try {
            return mapper.readTree(content);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e);
        }
    }
    
    public JsonNode createNode(Object content) {
        try {
            String json = mapper.writeValueAsString(content);
            try {
                return mapper.readTree(json);
            } catch (JsonMappingException e) {
                throw new JsonParsingException(e);
            }
        } catch (JsonProcessingException e) {
            throw new JsonSerializingException(e);
        }
    }

    public ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    public ArrayNode createArrayNode(String content) {
        try {
            return (ArrayNode) mapper.readTree(content);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e);
        }
    }

    public ArrayNode createArrayNode(Object content) {
        return (ArrayNode) createNode(content);
    }

    public String serialize(Object value) {
        try {
            return _getWriter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JsonSerializingException(e);
        }
    }

    public <T> T deserialize(String value, Class<T> clazz) {
        try {
            return mapper.readValue(sanitize(value), clazz);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e);
        }
    }

    public <T> T deserialize(String value, TypeReference<T> typeRef) {
        try {
            return mapper.readValue(sanitize(value), typeRef);
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e);
        }
    }

    public <T> T deserialize(JsonNode value, TypeReference<T> typeRef) {
        return mapper.convertValue(value, typeRef);
    }

    public <T> T deserialize(JsonNode value, Class<T> clazz) {
        return mapper.convertValue(value, clazz);
    }

    public JsonNode readValue(String value) {
        try {
            return mapper.readTree(sanitize(value));
        } catch (JsonProcessingException e) {
            throw new JsonParsingException(e);
        }
    }

    public JsonNode readValue(InputStream value) {
        try {
            return mapper.readTree(value);
        } catch (IOException e) {
            throw new JsonParsingException(e);
        }
    }

    public <T> T convertValue(Object value, Class<T> clazz) {
        return mapper.convertValue(value, clazz);
    }

    // if it uses the extended double quote character sometimes found in CSV files
    private String sanitize(String string) {
        if (string.startsWith("[“") || string.startsWith("{“")) {
            // replace both left and right versions
            return string.replace("“", "\"").replace("”", "\"");
        } else return string;
    }

    public void setIndent(boolean indent) {
        this.indent = indent;
    }

    private ObjectWriter _getWriter() {
        return (indent) ? mapper.writer(new DefaultPrettyPrinter()) : mapper.writer(new MinimalPrettyPrinter());
    }
}
