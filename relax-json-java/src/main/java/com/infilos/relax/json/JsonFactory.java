package com.infilos.relax.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import com.infilos.relax.Json;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static com.infilos.relax.json.JsonMappers.JavaMapper;

/**
 * @author infilos on 2020-06-13.
 */

public interface JsonFactory {

    ObjectMapper underMapper();

    default NullNode createNullNode() {
        return NullNode.getInstance();
    }

    default ArrayNode createArrayNode() {
        return underMapper().createArrayNode();
    }

    default ObjectNode createObjectNode() {
        return underMapper().createObjectNode();
    }

    default <T> TypeReference<T> createTypeReference() {
        return new TypeReference<T>() {};
    }

    default String escape(String jsonString) {
        if(StringUtils.isBlank(jsonString)) {
            return jsonString;
        }

        return StringEscapeUtils.escapeJson(jsonString);
    }

    default String unescape(String jsonString) {
        if(StringUtils.isBlank(jsonString)) {
            return jsonString;
        }

        return StringEscapeUtils.unescapeJson(jsonString);
    }

    default boolean isValidJsonString(String jsonString) {
        if(StringUtils.isBlank(jsonString)) {
            return false;
        }

        try {
            underMapper().readTree(jsonString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    default Json blankBlock() {
        return new Json(underMapper(), underMapper().createObjectNode());
    }

    default Json blankArray() {
        return new Json(underMapper(), underMapper().createArrayNode());
    }

    default Json blankString() {
        return new Json(underMapper(), TextNode.valueOf(""));
    }

    default Json from(String jsonString) {
        if(jsonString == null) {
            throw JsonException.of("ReadJsonFromString, Null");
        }
        if(StringUtils.isBlank(jsonString)) {
            return blankString();
        }

        try {
            return new Json(underMapper(),underMapper().readTree(jsonString));
        } catch (Exception ex) {
            try {
                return new Json(underMapper(), TextNode.valueOf(jsonString));
            } catch (Exception e) {
                throw JsonException.ofAction("ReadJsonFromString", ex);
            }
        }
    }

    default Json from(Map<String,Object> jsonMap) {
        try {
            return new Json(underMapper(), underMapper().valueToTree(jsonMap));
        } catch (Exception ex) {
            throw JsonException.ofAction("ReadJsonFromMap", ex);
        }
    }

    default Json from(Object javaObject) {
        try {
            if(javaObject instanceof Json) {
                return (Json) javaObject;
            }
            if(javaObject instanceof JsonNode) {
                return new Json(underMapper(), (JsonNode)javaObject);
            }
            if(javaObject instanceof String) {
                return from((String) javaObject);
            }
            return new Json(underMapper(), underMapper().valueToTree(javaObject));
        } catch (Exception ex) {
            throw JsonException.ofAction("ReadJsonFromObject", ex);
        }
    }

    default Json from(JsonNode jsonNode) {
        try {
            if(jsonNode.isNull() || jsonNode.isMissingNode()){
                throw JsonException.of("ReadJsonFromJsonNode, invalid JsonNode.");
            }

            return new Json(underMapper(), jsonNode);
        } catch (Exception ex) {
            throw JsonException.ofAction("ReadJsonFromJsonNode", ex);
        }
    }

    default Json from(byte[] jsonBytes) {
        try {
            return new Json(underMapper(), JavaMapper.readTree(jsonBytes));
        } catch (Exception ex) {
            throw JsonException.ofAction("ReadJsonFromBytes", ex);
        }
    }

    default JavaType typeOfGeneric(Class<?> outer, Class<?>...inners) {
        return underMapper().getTypeFactory().constructParametricType(outer, inners);
    }

    default JavaType typeOfGeneric(Class<?> outer, JavaType...inners) {
        return underMapper().getTypeFactory().constructParametricType(outer, inners);
    }

    default JavaType typeOfMap(Class<? extends Map<?,?>> map, Class<?> key, Class<?> value) {
        return underMapper().getTypeFactory().constructMapType(map, key, value);
    }

    default JavaType typeOfMap(Class<? extends Map<?,?>> map, JavaType key, JavaType value) {
        return underMapper().getTypeFactory().constructMapType(map, key, value);
    }

    default JavaType typeOfArray(Class<?> inner) {
        return underMapper().getTypeFactory().constructArrayType(inner);
    }

    default JavaType typeOfArray(JavaType inner) {
        return underMapper().getTypeFactory().constructArrayType(inner);
    }

    default JavaType typeOfCollection(Class<? extends Collection<?>> outer, Class<?> inner) {
        return underMapper().getTypeFactory().constructCollectionType(outer, inner);
    }

    default JavaType typeOfCollection(Class<? extends Collection<?>> outer, JavaType inner) {
        return underMapper().getTypeFactory().constructCollectionType(outer, inner);
    }
}
