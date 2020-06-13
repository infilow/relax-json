package com.infilos.relax;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.infilos.relax.json.JsonException;
import com.infilos.relax.json.JsonFactory;
import com.infilos.relax.json.JsonMappers;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author infilos on 2020-06-13.
 */

public final class Json extends JsonMappers {
    private JsonNode jsonNode;
    private ObjectMapper mapper;

    public Json(ObjectMapper mapper, JsonNode node) {
        this.mapper = mapper;
        this.jsonNode = node;
    }

    public String asString() {
        try {
            if (jsonNode.isTextual()) {
                return jsonNode.asText();
            }
            return mapper.writeValueAsString(jsonNode);
        } catch (Exception ex) {
            throw JsonException.ofAction("WriteJsonToString", ex);
        }
    }

    public String asPrettyString() {
        try {
            if (jsonNode.isTextual()) {
                return jsonNode.asText();
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception ex) {
            throw JsonException.ofAction("WriteJsonToString", ex);
        }
    }

    public Map<String, Object> asMap() {
        try {
            return mapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw JsonException.ofAction("WriteJsonToMap", ex);
        }
    }

    public <T> T asObject(Class<T> clazz) {
        try {
            return mapper.convertValue(jsonNode, clazz);
        } catch (Exception ex) {
            throw JsonException.ofAction("WriteJsonToObject", ex);
        }
    }

    public <T> T asType(TypeReference<T> typeReference) {
        try {
            return mapper.convertValue(jsonNode, typeReference);
        } catch (Exception ex) {
            throw JsonException.ofAction("WriteJsonToTypeRefer", ex);
        }
    }

    public <T> T asType(JavaType javaType) {
        try {
            return mapper.convertValue(jsonNode, javaType);
        } catch (Exception ex) {
            throw JsonException.ofAction("WriteJsonToJavaType", ex);
        }
    }

    public byte[] asBytes() {
        try {
            return mapper.writeValueAsBytes(jsonNode);
        } catch (Exception ex) {
            throw JsonException.ofAction("WriteJsonToBytes", ex);
        }
    }

    public JsonNode asJsonNode() {
        return copy().jsonNode;
    }

    public Json copy() {
        try {
            return new Json(mapper, mapper.valueToTree(jsonNode));
        } catch (Exception ex) {
            throw JsonException.ofAction("JsonDeepCopy", ex);
        }
    }

    public Json merge(Json overrides) {
        try {
            return from((JsonNode) mapper.readerForUpdating(jsonNode).readValue(overrides.asJsonNode()));
        } catch (Exception ex) {
            throw JsonException.ofAction("JsonMerge", ex);
        }
    }

    @Override
    public String toString() {
        return this.asString();
    }

    @Override
    public int hashCode() {
        return jsonNode.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (Objects.isNull(object)) {
            return false;
        }
        if (!(object instanceof Json)) {
            return false;
        }

        return jsonNode.equals(((Json) object).jsonNode);
    }


    // Below are factories from JsonFactory.

    private static final JsonFactory Factory = () -> JsonMappers.JavaMapper;

    public static ObjectMapper underMapper() {
        return Factory.underMapper();
    }

    public static ObjectNode createObjectNode() {
        return Factory.createObjectNode();
    }

    public static <T> TypeReference<T> createTypeReference() {
        return Factory.createTypeReference();
    }

    public static String escape(String jsonString) {
        return Factory.escape(jsonString);
    }

    public static String unescape(String jsonString) {
        return Factory.unescape(jsonString);
    }

    public static boolean isValidJsonString(String jsonString) {
        return Factory.isValidJsonString(jsonString);
    }

    public static Json blankBlock() {
        return Factory.blankBlock();
    }

    public static Json blankArray() {
        return Factory.blankArray();
    }

    public static Json blankString() {
        return Factory.blankString();
    }

    public static Json from(String jsonString) {
        return Factory.from(jsonString);
    }

    public static Json from(Map<String, Object> jsonMap) {
        return Factory.from(jsonMap);
    }

    public static Json from(Object javaObject) {
        return Factory.from(javaObject);
    }

    public static Json from(JsonNode jsonNode) {
        return Factory.from(jsonNode);
    }

    public static Json from(byte[] jsonBytes) {
        return Factory.from(jsonBytes);
    }

    public static JavaType typeOfGeneric(Class<?> outer, Class<?>... inners) {
        return Factory.typeOfGeneric(outer, inners);
    }

    public static JavaType typeOfGeneric(Class<?> outer, JavaType... inners) {
        return Factory.typeOfGeneric(outer, inners);
    }

    public static JavaType typeOfMap(Class<? extends Map<?,?>> map, Class<?> key, Class<?> value) {
        return Factory.typeOfMap(map, key, value);
    }

    public static JavaType typeOfMap(Class<? extends Map<?,?>> map, JavaType key, JavaType value) {
        return Factory.typeOfMap(map, key, value);
    }

    public static JavaType typeOfArray(Class<?> inner) {
        return Factory.typeOfArray(inner);
    }

    public static JavaType typeOfArray(JavaType inner) {
        return Factory.typeOfArray(inner);
    }

    public static JavaType typeOfCollection(Class<? extends Collection<?>> outer, Class<?> inner) {
        return Factory.typeOfCollection(outer, inner);
    }

    public static JavaType typeOfCollection(Class<? extends Collection<?>> outer, JavaType inner) {
        return Factory.typeOfCollection(outer, inner);
    }
}
