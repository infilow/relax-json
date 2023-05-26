package com.infilos.relax;

import static com.infilos.relax.flat.FlattenMode.MONGO;
import static java.util.Collections.EMPTY_MAP;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.infilos.relax.flat.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Flattens any JSON nested objects or arrays into a flattened JSON string or a Map{@literal <Stirng, Object>}.
 * <br>
 * The String key will represents the corresponding position of value in the original nested objects or arrays and the Object value are either String, Boolean, Long, Double or null. <br>
 * <br>
 * For example:<br> A nested JSON <br>
 * { "a" : { "b" : 1, "c": null, "d": [false, true] }, "e": "f", "g":2.3 }<br>
 * <br>
 * can be turned into a flattened JSON <br>
 * { "a.b": 1, "a.c": null, "a.d[0]": false, "a.d[1]": true, "e": "f", "g":2.3 }
 * <br>
 * <br>
 * or into a Map<br> {<br> &nbsp;&nbsp;a.b=1,<br> &nbsp;&nbsp;a.c=null,<br> &nbsp;&nbsp;a.d[0]=false,<br> &nbsp;&nbsp;a.d[1]=true,<br> &nbsp;&nbsp;e=f,<br> &nbsp;&nbsp;g=2.3<br> }
 */
public final class JsonFlattener {
    /**
     * <pre>
     * "root" is the default key of the Map returned by #flattenAsMap.
     * When JsonFlattener processes a JSON string which is not a JSON object or array, the final outcome may not suit in a Java Map.
     * At that moment, JsonFlattener will put the result in the Map with "root" as its key.
     * </pre>
     */
    public static final String ROOT = "root";

    /**
     * Returns a flattened JSON string.
     *
     * @param jsonVal a JSON data which wraps by JsonNode
     * @return a flattened JSON string
     */
    public static String flatten(JsonNode jsonVal) {
        return new JsonFlattener(jsonVal).flatten();
    }

    /**
     * Returns a flattened JSON string.
     *
     * @param json the JSON string
     * @return a flattened JSON string
     */
    public static String flatten(String json) {
        return new JsonFlattener(json).flatten();
    }

    /**
     * Returns a flattened JSON as Map.
     *
     * @param jsonVal a JSON data which wraps by JsonNode
     * @return a flattened JSON as Map
     */
    public static Map<String, Object> flattenAsMap(JsonNode jsonVal) {
        return new JsonFlattener(jsonVal).flattenAsMap();
    }

    /**
     * Returns a flattened JSON as Map.
     *
     * @param json the JSON string
     * @return a flattened JSON as Map
     */
    public static Map<String, Object> flattenAsMap(String json) {
        return new JsonFlattener(json).flattenAsMap();
    }

    private final JsonNode source;
    private final Deque<IndexedIterator<?>> elementItors = new ArrayDeque<>();

    private JsonifyLinkedHashMap<String, Object> flattenedMap;

    private FlattenMode flattenMode = FlattenMode.NORMAL;
    private TranslatorFactory policy = StringEscapePolicy.DEFAULT;
    private Character separator = '.';
    private Character leftBracket = '[';
    private Character rightBracket = ']';
    private PrintMode printMode = PrintMode.MINIMAL;
    private KeyTransformer keyTrans = null;
    private boolean ignoreReservedCharacters = false;

    private JsonFlattener newJsonFlattener(JsonNode jsonVal) {
        JsonFlattener jf = new JsonFlattener(jsonVal);
        jf.withFlattenMode(flattenMode);
        jf.withStringEscapePolicy(policy);
        jf.withSeparator(separator);
        jf.withLeftAndRightBrackets(leftBracket, rightBracket);
        jf.withPrintMode(printMode);
        if (keyTrans != null) jf.withKeyTransformer(keyTrans);
        if (ignoreReservedCharacters) jf.ignoreReservedCharacters();
        return jf;
    }

    public JsonFlattener(JsonNode json) {
        this.source = notNull(json);
    }

    public JsonFlattener(String json) {
        this.source = Json.from(json).asJsonNode();
    }

    /**
     * A fluent setter to setup a mode of the {@link JsonFlattener}.
     *
     * @param flattenMode a {@link FlattenMode}
     * @return this {@link JsonFlattener}
     */
    public JsonFlattener withFlattenMode(FlattenMode flattenMode) {
        this.flattenMode = notNull(flattenMode);
        this.flattenedMap = null;

        return this;
    }

    /**
     * A fluent setter to setup the JSON string escape policy.
     *
     * @param policy any {@link TranslatorFactory} or a {@link StringEscapePolicy}
     * @return this {@link JsonFlattener}
     */
    public JsonFlattener withStringEscapePolicy(TranslatorFactory policy) {
        this.policy = notNull(policy);
        this.flattenedMap = null;

        return this;
    }

    /**
     * A fluent setter to setup the separator within a key in the flattened JSON. The default separator is a dot(.).
     *
     * @param separator any character
     * @return this {@link JsonFlattener}
     */
    public JsonFlattener withSeparator(char separator) {
        String separatorStr = String.valueOf(separator);
        isTrue(!separatorStr.matches("[\"\\s]"), "Separator contains illegal character(%s)", separatorStr);
        isTrue(!leftBracket.equals(separator) && !rightBracket.equals(separator), "Separator(%s) is already used in brackets", separatorStr);

        this.separator = separator;
        this.flattenedMap = null;

        return this;
    }

    private String illegalBracketsRegex() {
        return "[\"\\s" + Pattern.quote(this.separator.toString()) + "]";
    }

    /**
     * A fluent setter to setup the left and right brackets within a key in the flattened JSON. The default left and right brackets are left square bracket([) and right square bracket(]).
     *
     * @param leftBracket  any character
     * @param rightBracket any character
     * @return this {@link JsonFlattener}
     */
    public JsonFlattener withLeftAndRightBrackets(char leftBracket, char rightBracket) {
        isTrue(leftBracket != rightBracket, "Both brackets cannot be the same");

        String leftBracketStr = String.valueOf(leftBracket);
        String rightBracketStr = String.valueOf(rightBracket);
        isTrue(!leftBracketStr.matches(illegalBracketsRegex()), "Left bracket contains illegal character(%s)", leftBracketStr);
        isTrue(!rightBracketStr.matches(illegalBracketsRegex()), "Right bracket contains illegal character(%s)", rightBracketStr);

        this.leftBracket = leftBracket;
        this.rightBracket = rightBracket;
        this.flattenedMap = null;

        return this;
    }

    /**
     * A fluent setter to setup a print mode of the {@link JsonFlattener}. The default print mode is minimal.
     *
     * @param printMode a {@link PrintMode}
     * @return this {@link JsonFlattener}
     */
    public JsonFlattener withPrintMode(PrintMode printMode) {
        this.printMode = notNull(printMode);
        return this;
    }

    /**
     * A fluent setter to setup a {@link KeyTransformer} of the {@link JsonFlattener}.
     *
     * @param keyTrans a {@link KeyTransformer}
     * @return this {@link JsonFlattener}
     */
    public JsonFlattener withKeyTransformer(KeyTransformer keyTrans) {
        this.keyTrans = notNull(keyTrans);
        this.flattenedMap = null;
        return this;
    }

    /**
     * After this option is enable, all reserved characters used in keys will stop to be checked and escaped. <br>
     * <br>
     * Example:<br>
     * <br>
     * Input JSON: {"matrix":{"agent.smith":"1999"}}<br> Flatten with option disable: {"matrix[\"agent.smith\"]":"1999"}<br> Flatten with option enable: {"matrix.agent.smith":"1999"}<br>
     * <br>
     * {@link JsonCompactor} may cause unpredictable results with the JSON produced by a {@link JsonFlattener} with this option enable.
     *
     * @return this {@link JsonFlattener}
     */
    public JsonFlattener ignoreReservedCharacters() {
        ignoreReservedCharacters = true;
        return this;
    }

    /**
     * Returns a flattened JSON string.
     *
     * @return a flattened JSON string
     */
    public String flatten() {
        flattenAsMap();

        if (source.isObject() || isObjectifiableArray()) {
            return flattenedMap.toString(printMode);
        } else {
            return javaObj2Json(flattenedMap.get(ROOT));
        }
    }

    private boolean isObjectifiableArray() {
        return source.isArray() && !flattenedMap.containsKey(ROOT);
    }

    private String javaObj2Json(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof CharSequence) {
            return '"' + policy.getTranslator().translate((CharSequence) obj) + '"';
        } else if (obj instanceof JsonifyArrayList) {
            return ((JsonifyArrayList<?>) obj).toString(printMode);
        } else {
            return obj.toString();
        }
    }

    /**
     * Returns a flattened JSON as Map.
     *
     * @return a flattened JSON as Map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> flattenAsMap() {
        if (flattenedMap != null) {
            return flattenedMap;
        }

        flattenedMap = createJsonifyLinkedHashMap();
        reduce(source);

        while (!elementItors.isEmpty()) {
            IndexedIterator<?> deepestIter = elementItors.getLast();
            if (!deepestIter.hasNext()) {
                elementItors.removeLast();
            } else if (deepestIter.peek() instanceof Entry) {
                Entry<String, ? extends JsonNode> mem = (Entry<String, ? extends JsonNode>) deepestIter.next();
                reduce(mem.getValue());
            } else { // JsonValue
                JsonNode val = (JsonNode) deepestIter.next();
                reduce(val);
            }
        }

        return flattenedMap;
    }

    private void reduce(JsonNode val) {
        if (val.isObject() && val.iterator().hasNext()) {
            ObjectNode objectNode = (ObjectNode) val;
            elementItors.add(new IndexedIterator<>(objectNode.fields()));
        } else if (val.isArray() && val.iterator().hasNext()) {
            switch (flattenMode) {
                case KEEP_PRIMITIVE_ARRAYS:
                    boolean allPrimitive = true;
                    for (JsonNode value : val) {
                        if (value.isArray() || value.isObject()) {
                            allPrimitive = false;
                            break;
                        }
                    }

                    if (allPrimitive) {
                        JsonifyArrayList<Object> array = createJsonifyArrayList();
                        for (JsonNode value : val) {
                            array.add(jsonVal2Obj(value));
                        }
                        flattenedMap.put(computeKey(), array);
                    } else {
                        elementItors.add(IndexedIterator.from(val));
                    }
                    break;
                case KEEP_ARRAYS:
                    JsonifyArrayList<Object> array = createJsonifyArrayList();
                    for (JsonNode value : val) {
                        array.add(jsonVal2Obj(value));
                    }
                    flattenedMap.put(computeKey(), array);
                    break;
                default:
                    elementItors.add(IndexedIterator.from(val));
            }
        } else {
            String key = computeKey();
            Object value = jsonVal2Obj(val);
            // Check NOT empty JSON object
            if (!ROOT.equals(key) || !EMPTY_MAP.equals(value)) {
                flattenedMap.put(key, value);
            }
        }
    }

    private Object jsonVal2Obj(JsonNode val) {
        if (val.isBoolean()) {
            return val.booleanValue();
        }
        if (val.isTextual()) {
            return val.textValue();
        }
        if (val.isNumber()) {
            return val.numberValue();
        }
        if (flattenMode == FlattenMode.KEEP_ARRAYS) {
            if (val.isArray()) {
                JsonifyArrayList<Object> array = createJsonifyArrayList();
                for (JsonNode value : val) {
                    array.add(jsonVal2Obj(value));
                }
                return array;
            } else if (val.isObject()) {
                if (val.iterator().hasNext()) {
                    return newJsonFlattener(val).flattenAsMap();
                } else {
                    return createJsonifyLinkedHashMap();
                }
            }
        } else {
            if (val.isArray()) {
                return createJsonifyArrayList();
            } else if (val.isObject()) {
                return createJsonifyLinkedHashMap();
            }
        }

        return null;
    }

    private boolean hasReservedCharacters(String key) {
        if (flattenMode.equals(MONGO) && StringUtils.containsAny(key, separator))
            throw new IllegalArgumentException("Key cannot contain separator("
                + separator + ") in FlattenMode." + MONGO);

        return StringUtils.containsAny(key, separator, leftBracket, rightBracket);
    }

    @SuppressWarnings("unchecked")
    private String computeKey() {
        if (elementItors.isEmpty()) {
            return ROOT;
        }

        StringBuilder sb = new StringBuilder();

        for (IndexedIterator<?> itor : elementItors) {
            if (itor.getCurrent() instanceof Entry) {
                String key = ((Entry<String, ? extends JsonNode>) itor.getCurrent()).getKey();

                if (keyTrans != null) {
                    key = keyTrans.transform(key);
                }
                if (!ignoreReservedCharacters && hasReservedCharacters(key)) {
                    sb.append(leftBracket);
                    sb.append('"');
                    sb.append(policy.getTranslator().translate(key));
                    sb.append('"');
                    sb.append(rightBracket);
                } else {
                    if (sb.length() != 0) {
                        sb.append(separator);
                    }
                    sb.append(key);
                }
            } else { // JsonValue
                sb.append(flattenMode.equals(MONGO) ? separator : leftBracket);
                sb.append(itor.getIndex());
                sb.append(flattenMode.equals(MONGO) ? "" : rightBracket);
            }
        }

        return sb.toString();
    }

    private <T> JsonifyArrayList<T> createJsonifyArrayList() {
        JsonifyArrayList<T> array = new JsonifyArrayList<>();
        array.setTranslator(policy.getTranslator());
        return array;
    }

    private <K, V> JsonifyLinkedHashMap<K, V> createJsonifyLinkedHashMap() {
        JsonifyLinkedHashMap<K, V> map = new JsonifyLinkedHashMap<>();
        map.setTranslator(policy.getTranslator());

        return map;
    }

    @Override
    public int hashCode() {
        return 31 * 27 + source.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JsonFlattener)) {
            return false;
        }
        return source.equals(((JsonFlattener) o).source);
    }

    @Override
    public String toString() {
        return "JsonFlattener{source=" + source + "}";
    }
}
