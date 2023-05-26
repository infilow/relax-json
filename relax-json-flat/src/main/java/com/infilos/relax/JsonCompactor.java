package com.infilos.relax;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import com.infilos.relax.flat.*;

import static com.infilos.relax.flat.FlattenMode.MONGO;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Compact any flattened JSON string back to nested one.
 */
public final class JsonCompactor {
    /**
     * <pre>
     * "root" is the default key of the Map returned by {@link #compactAsMap}.
     * When JsonCompactor processes a JSON string which is not a JSON object or array, the final outcome may not suit in a Java Map.
     * At that moment, JsonCompactor will put the result in the Map with "root" as its key.
     * </pre>
     */
    public static final String ROOT = "root";

    /**
     * Returns a JSON string of nested objects by the given flattened JSON string.
     *
     * @param json a flattened JSON string
     * @return a JSON string of nested objects
     */
    public static String compact(String json) {
        return new JsonCompactor(json).compact();
    }

    /**
     * Returns a JSON string of nested objects by the given flattened Map.
     *
     * @param flattenedMap a flattened Map
     * @return a JSON string of nested objects
     */
    public static String compact(Map<String, ?> flattenedMap) {
        return new JsonCompactor(flattenedMap).compact();
    }

    /**
     * Returns a Java Map of nested objects by the given flattened JSON string.
     *
     * @param json a flattened JSON string
     * @return a Java Map of nested objects
     */
    public static Map<String, Object> compactAsMap(String json) {
        return new JsonCompactor(json).compactAsMap();
    }

    /**
     * Returns a Java Map of nested objects by the given flattened Map.
     *
     * @param flattenedMap a flattened Map
     * @return a Java Map of nested objects
     */
    public static Map<String, Object> compactAsMap(Map<String, ?> flattenedMap) {
        return new JsonCompactor(flattenedMap).compactAsMap();
    }

    
    private final JsonNode root;

    private FlattenMode flattenMode = FlattenMode.NORMAL;
    private Character separator = '.';
    private Character leftBracket = '[';
    private Character rightBracket = ']';
    private PrintMode printMode = PrintMode.MINIMAL;
    private KeyTransformer keyTransformer = null;

    private JsonCompactor createJsonCompactor(JsonNode jsonValue) {
        JsonCompactor compactor = new JsonCompactor(jsonValue);
        compactor.withFlattenMode(flattenMode);
        compactor.withSeparator(separator);
        compactor.withLeftAndRightBrackets(leftBracket, rightBracket);
        compactor.withPrintMode(printMode);
        if (keyTransformer != null) {
            compactor.withKeyTransformer(keyTransformer);
        }

        return compactor;
    }

    private JsonCompactor(JsonNode root) {
        this.root = root;
    }

    public JsonCompactor(String json) {
        this.root = Json.from(json).asJsonNode();
    }

    /**
     * Creates a JSON compactor by given flattened Map.
     *
     * @param flattenedMap a flattened Map
     */
    public JsonCompactor(Map<String, ?> flattenedMap) {
        this.root = Json.from(new JsonifyLinkedHashMap<>(flattenedMap).toString()).asJsonNode();
    }

    private String arrayIndex() {
        return Pattern.quote(leftBracket.toString()) + "\\s*\\d+\\s*" + Pattern.quote(rightBracket.toString());
    }

    private String objectComplexKey() {
        return Pattern.quote(leftBracket.toString()) + "\\s*\".+?\"\\s*" + Pattern.quote(rightBracket.toString());
    }

    private String objectKey() {
        return "[^" + Pattern.quote(separator.toString())
            + Pattern.quote(leftBracket.toString())
            + Pattern.quote(rightBracket.toString()) + "]+";
    }

    private Pattern keyPartPattern() {
        if (flattenMode.equals(MONGO)) {
            return Pattern.compile("[^" + Pattern.quote(separator.toString()) + "]+");
        } else {
            return Pattern.compile(arrayIndex() + "|" + objectComplexKey() + "|" + objectKey());
        }
    }

    /**
     * A fluent setter to setup a mode of the JsonCompactor.
     *
     * @param flattenMode a FlattenMode
     * @return this JsonCompactor
     */
    public JsonCompactor withFlattenMode(FlattenMode flattenMode) {
        this.flattenMode = notNull(flattenMode);
        return this;
    }

    /**
     * A fluent setter to setup the separator within a key in the flattened JSON. The default separator is a dot(.).
     *
     * @param separator any character
     * @return this JsonCompactor
     */
    public JsonCompactor withSeparator(char separator) {
        String separatorStr = String.valueOf(separator);
        isTrue(!separatorStr.matches("[\"\\s]"), "Separator contains illegal character(%s)", separatorStr);
        isTrue(
            !leftBracket.equals(separator) && !rightBracket.equals(separator),
            "Separator(%s) is already used in brackets", separatorStr
        );
        this.separator = separator;

        return this;
    }

    private String illegalBracketsRegex() {
        return "[\"\\s" + Pattern.quote(separator.toString()) + "]";
    }

    /**
     * A fluent setter to setup the left and right brackets within a key in the flattened JSON. The default left and right brackets are left square bracket([) and right square bracket(]).
     *
     * @param leftBracket  any character
     * @param rightBracket any character
     * @return this JsonCompactor
     */
    public JsonCompactor withLeftAndRightBrackets(char leftBracket, char rightBracket) {
        isTrue(leftBracket != rightBracket, "Both brackets cannot be the same");
        String leftBracketStr = String.valueOf(leftBracket);
        String rightBracketStr = String.valueOf(rightBracket);
        isTrue(!leftBracketStr.matches(illegalBracketsRegex()),
            "Left bracket contains illegal character(%s)", leftBracketStr);
        isTrue(!rightBracketStr.matches(illegalBracketsRegex()),
            "Right bracket contains illegal character(%s)", rightBracketStr);
        this.leftBracket = leftBracket;
        this.rightBracket = rightBracket;

        return this;
    }

    /**
     * A fluent setter to setup a print mode of the JsonCompactor. The default print mode is minimal.
     *
     * @param printMode a PrintMode
     * @return this JsonCompactor
     */
    public JsonCompactor withPrintMode(PrintMode printMode) {
        this.printMode = notNull(printMode);
        return this;
    }

    /**
     * A fluent setter to setup a KeyTransformer of the JsonCompactor.
     *
     * @param keyTrans a KeyTransformer
     * @return this JsonCompactor
     */
    public JsonCompactor withKeyTransformer(KeyTransformer keyTrans) {
        this.keyTransformer = notNull(keyTrans);
        return this;
    }

    private String writeByConfig(JsonNode jsonValue) {
        if (printMode == PrintMode.PRETTY) {
            return Json.from(jsonValue).asPrettyString();
        }

        return Json.from(jsonValue).asString();
    }

    /**
     * Returns a JSON string of nested objects by the given flattened JSON string.
     *
     * @return a JSON string of nested objects
     */
    public String compact() {
        StringWriter sw = new StringWriter();
        if (root.isArray()) {
            ArrayNode compactedArray = compactArray((ArrayNode) root);
            sw.append(writeByConfig(compactedArray));
            return sw.toString();
        }
        if (!root.isObject()) {
            return root.toString();
        }

        ObjectNode flattened = (ObjectNode) root;
        JsonNode compacted = flattened.isEmpty() ? Json.createObjectNode() : null;

        Iterator<String> names = flattened.fieldNames();
        while (names.hasNext()) {
            String key = names.next();
            JsonNode currentVal = compacted;
            String objKey = null;
            Integer aryIdx = null;

            Matcher matcher = keyPartPattern().matcher(key);
            while (matcher.find()) {
                String keyPart = matcher.group();

                if (objKey != null ^ aryIdx != null) {
                    if (isJsonArray(keyPart)) {
                        currentVal =
                            findOrCreateJsonArray(currentVal, objKey, aryIdx);
                        objKey = null;
                        aryIdx = extractIndex(keyPart);
                    } else { // JSON object
                        if (flattened.get(key).isArray()) { // KEEP_ARRAYS mode
                            flattened.set(key, compactArray((ArrayNode) flattened.get(key)));
                        }
                        currentVal = findOrCreateJsonObject(currentVal, objKey, aryIdx);
                        objKey = extractKey(keyPart);
                        aryIdx = null;
                    }
                }
                if (objKey == null && aryIdx == null) {
                    if (isJsonArray(keyPart)) {
                        aryIdx = extractIndex(keyPart);
                        if (currentVal == null) currentVal = Json.createArrayNode();
                    } else { // JSON object
                        objKey = extractKey(keyPart);
                        if (currentVal == null) currentVal = Json.createObjectNode();
                    }
                }
                if (compacted == null) {
                    compacted = currentVal;
                }
            }

            setCompactedValue(flattened, key, currentVal, objKey, aryIdx);
        }
        sw.append(writeByConfig(compacted));

        return sw.toString();
    }

    /**
     * Returns a Java Map of nested objects by the given flattened JSON string.
     *
     * @return a Java Map of nested objects
     */
    public Map<String, Object> compactAsMap() {
        JsonNode flattenedValue = Json.from(compact()).asJsonNode();
        if (flattenedValue.isArray() || !flattenedValue.isObject()) {
            ObjectNode jsonObj = Json.createObjectNode();
            jsonObj.set(ROOT, flattenedValue);

            return Json.from(jsonObj).asMap();
        } else {
            return Json.from(flattenedValue).asMap();
        }
    }

    private ArrayNode compactArray(ArrayNode array) {
        ArrayNode compactedArray = Json.createArrayNode();

        for (JsonNode value : array) {
            if (value.isArray()) {
                compactedArray.add(compactArray((ArrayNode) value));
            } else if (value.isObject()) {
                JsonNode obj;
                obj = Json.from(createJsonCompactor(value).compact()).asJsonNode();
                compactedArray.add(obj);
            } else {
                compactedArray.add(value);
            }
        }

        return compactedArray;
    }

    private String extractKey(String keyPart) {
        if (keyPart.matches(objectComplexKey())) {
            keyPart = keyPart.replaceAll("^" + Pattern.quote(leftBracket.toString()) + "\\s*\"", "");
            keyPart = keyPart.replaceAll("\"\\s*" + Pattern.quote(rightBracket.toString()) + "$", "");
        }

        return keyTransformer != null ? keyTransformer.transform(keyPart) : keyPart;
    }

    private Integer extractIndex(String keyPart) {
        if (flattenMode.equals(MONGO)) {
            return Integer.valueOf(keyPart);
        } else {
            return Integer.valueOf(
                keyPart.replaceAll("[" + Pattern.quote(leftBracket.toString()) + Pattern.quote(rightBracket.toString()) + "\\s]", "")
            );
        }
    }

    private boolean isJsonArray(String keyPart) {
        return keyPart.matches(arrayIndex()) || (flattenMode.equals(MONGO) && keyPart.matches("\\d+"));
    }

    private ArrayNode findOrCreateJsonArray(JsonNode currentVal, String objKey, Integer aryIdx) {
        if (objKey != null) {
            ObjectNode jsonObj = (ObjectNode) currentVal;

            if (jsonObj.get(objKey) == null) {
                ArrayNode ary = Json.underMapper().createArrayNode();
                jsonObj.set(objKey, ary);

                return ary;
            }

            return (ArrayNode) jsonObj.get(objKey);
        } else { // aryIdx != null
            ArrayNode jsonAry = (ArrayNode) currentVal;

            if (jsonAry.size() <= aryIdx || jsonAry.get(aryIdx).isNull()) {
                ArrayNode ary = Json.createArrayNode();
                assureJsonArraySize(jsonAry, aryIdx);
                jsonAry.set(aryIdx, ary);

                return ary;
            }

            return (ArrayNode) jsonAry.get(aryIdx);
        }
    }

    private ObjectNode findOrCreateJsonObject(JsonNode currentVal, String objKey, Integer aryIdx) {
        if (objKey != null) {
            ObjectNode jsonObj = (ObjectNode) currentVal;

            if (jsonObj.get(objKey) == null) {
                ObjectNode obj = Json.createObjectNode();
                jsonObj.set(objKey, obj);

                return obj;
            }

            return (ObjectNode) jsonObj.get(objKey);
        } else { // aryIdx != null
            ArrayNode jsonAry = (ArrayNode) currentVal;

            if (jsonAry.size() <= aryIdx || jsonAry.get(aryIdx).isNull()) {
                ObjectNode obj = Json.createObjectNode();
                assureJsonArraySize(jsonAry, aryIdx);
                jsonAry.set(aryIdx, obj);

                return obj;
            }

            return (ObjectNode) jsonAry.get(aryIdx);
        }
    }

    private void setCompactedValue(ObjectNode flattened, String key,
                                   JsonNode currentVal, String objKey, Integer aryIdx) {
        JsonNode val = flattened.get(key);
        if (objKey != null) {
            if (val.isArray()) {
                ArrayNode jsonArray = Json.underMapper().createArrayNode();
                for (JsonNode arrayVal : val) {
                    jsonArray.add(Json.from(createJsonCompactor(arrayVal).compact()).asJsonNode());
                }
                ((ObjectNode) currentVal).set(objKey, jsonArray);
            } else {
                ((ObjectNode) currentVal).set(objKey, val);
            }
        } else { // aryIdx != null
            assureJsonArraySize((ArrayNode) currentVal, aryIdx);
            ((ArrayNode) currentVal).set(aryIdx, val);
        }
    }

    private void assureJsonArraySize(ArrayNode jsonArray, Integer index) {
        while (index >= jsonArray.size()) {
            jsonArray.add(NullNode.getInstance());
        }
    }

    @Override
    public int hashCode() {
        return 31 * 27 + root.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonCompactor)) {
            return false;
        }

        return root.equals(((JsonCompactor) o).root);
    }

    @Override
    public String toString() {
        return "JsonCompactor{root=" + root + "}";
    }
}
