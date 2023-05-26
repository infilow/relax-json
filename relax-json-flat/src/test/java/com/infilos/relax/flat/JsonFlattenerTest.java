package com.infilos.relax.flat;

import java.io.IOException;
import java.util.*;

import com.infilos.relax.*;
import com.infilos.utils.Resource;
import org.junit.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class JsonFlattenerTest extends Assert {

    ObjectMapper mapper = new ObjectMapper();

    //@Test
    //public void testConstructorException() {
    //    assertThrows(RuntimeException.class, () -> {
    //        new JsonFlattener("abc[123]}");
    //    });
    //}

    @Test
    public void testFlatten() throws IOException {
        String json = Resource.readAsString("test2.json");

        assertEquals(
            "{\"a.b\":1,\"a.c\":null,\"a.d[0]\":false,\"a.d[1]\":true,\"e\":\"f\",\"g\":2.3}",
            JsonFlattener.flatten(json));

        assertEquals("{\"[0].a\":1,\"[1]\":2,\"[2].c[0]\":3,\"[2].c[1]\":4}",
            JsonFlattener.flatten("[{\"a\":1},2,{\"c\":[3,4]}]"));
    }

    @Test
    public void testFlattenAsMap() throws IOException {
        String json = Resource.readAsString("test2.json");

        assertEquals(
            "{\"a.b\":1,\"a.c\":null,\"a.d[0]\":false,\"a.d[1]\":true,\"e\":\"f\",\"g\":2.3}",
            JsonFlattener.flattenAsMap(json).toString());
    }

    @Test
    public void testFlattenWithJsonValueBase() throws IOException {
        String json = Resource.readAsString("test2.json");

        JsonNode jsonVal = new ObjectMapper().readTree(json);
        assertEquals(
            "{\"a.b\":1,\"a.c\":null,\"a.d[0]\":false,\"a.d[1]\":true,\"e\":\"f\",\"g\":2.3}",
            JsonFlattener.flatten(jsonVal));

        assertEquals("{\"[0].a\":1,\"[1]\":2,\"[2].c[0]\":3,\"[2].c[1]\":4}",
            JsonFlattener.flatten("[{\"a\":1},2,{\"c\":[3,4]}]"));
    }

    @Test
    public void testFlattenAsMapWithJsonValueBase() throws IOException {
        String json = Resource.readAsString("test2.json");

        JsonNode jsonVal = new ObjectMapper().readTree(json);
        assertEquals(
            "{\"a.b\":1,\"a.c\":null,\"a.d[0]\":false,\"a.d[1]\":true,\"e\":\"f\",\"g\":2.3}",
            JsonFlattener.flattenAsMap(jsonVal).toString());

        assertEquals("{\"[0].a\":1,\"[1]\":2,\"[2].c[0]\":3,\"[2].c[1]\":4}",
            JsonFlattener.flattenAsMap("[{\"a\":1},2,{\"c\":[3,4]}]").toString());
    }

    @Test
    public void testFlattenWithKeyContainsDotAndSquareBracket()
        throws IOException {
        assertEquals(
            "{\"[0][\\\"a.a.[\\\"]\":1,\"[1]\":2,\"[2].c[0]\":3,\"[2].c[1]\":4}",
            JsonFlattener.flatten("[{\"a.a.[\":1},2,{\"c\":[3,4]}]"));
    }

    @Test
    public void testHashCode() throws IOException {
        String json1 = Resource.readAsString("test.json");
        String json2 = Resource.readAsString("test2.json");

        JsonFlattener flattener = new JsonFlattener(json1);
        assertEquals(flattener.hashCode(), flattener.hashCode());
        assertEquals(flattener.hashCode(), new JsonFlattener(json1).hashCode());
        assertNotEquals(flattener.hashCode(), new JsonFlattener(json2).hashCode());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEquals() throws IOException {
        String json1 = Resource.readAsString("test.json");
        String json2 = Resource.readAsString("test2.json");

        JsonFlattener flattener = new JsonFlattener(json1);
        assertTrue(flattener.equals(flattener));
        assertTrue(flattener.equals(new JsonFlattener(json1)));
        assertFalse(flattener.equals(new JsonFlattener(json2)));
        assertFalse(flattener.equals(123L));
    }

    @Test
    public void testToString() throws IOException {
        String json = Resource.readAsString("test2.json");

        assertEquals(
            "JsonFlattener{source={\"a\":{\"b\":1,\"c\":null,\"d\":[false,true]},\"e\":\"f\",\"g\":2.3}}",
            new JsonFlattener(json).toString());
    }

    @Test
    public void testWithNoPrecisionDouble() throws IOException {
        String json = "{\"39473331\":{\"mega\":6.0,\"goals\":1.0}}";
        assertEquals("{\"39473331.mega\":6.0,\"39473331.goals\":1.0}",
            new JsonFlattener(json).flatten());
    }

    @Test
    public void testWithEmptyJsonObject() throws IOException {
        String json = "{}";
        assertEquals("{}", new JsonFlattener(json).flatten());
        assertEquals(json,
            JsonCompactor.compact(new JsonFlattener(json).flatten()));
        assertEquals(new HashMap<>(), new JsonFlattener(json).flattenAsMap());
    }

    @Test
    public void testWithEmptyJsonArray() throws IOException {
        String json = "[]";
        assertEquals("[]", new JsonFlattener(json).flatten());
        assertEquals(mapOf("root", new ArrayList<>()),
            new JsonFlattener(json).flattenAsMap());
        assertEquals(json,
            JsonCompactor.compact(new JsonFlattener(json).flatten()));
        assertEquals("[]", new JsonFlattener(json)
            .withFlattenMode(FlattenMode.KEEP_ARRAYS).flatten());
        assertEquals(mapOf("root", new ArrayList<>()),
            new JsonFlattener(json).withFlattenMode(FlattenMode.KEEP_ARRAYS)
                .flattenAsMap());
        assertEquals(json, JsonCompactor.compact(new JsonFlattener(json)
            .withFlattenMode(FlattenMode.KEEP_ARRAYS).flatten()));
    }

    @Test
    public void testWithEmptyArray() {
        String json = "{\"no\":\"1\",\"name\":\"riya\",\"marks\":[]}";
        assertEquals("{\"no\":\"1\",\"name\":\"riya\",\"marks\":[]}",
            new JsonFlattener(json).flatten());
        assertEquals(json,
            JsonCompactor.compact(new JsonFlattener(json).flatten()));
    }

    @Test
    public void testWithEmptyObject() {
        String json = "{\"no\":\"1\",\"name\":\"riya\",\"marks\":[{}]}";
        assertEquals("{\"no\":\"1\",\"name\":\"riya\",\"marks[0]\":{}}",
            new JsonFlattener(json).flatten());
        assertEquals(json,
            JsonCompactor.compact(new JsonFlattener(json).flatten()));
    }

    @Test
    public void testWithArray() {
        String json = "[{\"abc\":123},456,[null]]";
        assertEquals("{\"[0].abc\":123,\"[1]\":456,\"[2][0]\":null}",
            new JsonFlattener(json).flatten());
        assertEquals(json,
            JsonCompactor.compact(new JsonFlattener(json).flatten()));
    }

    @Test
    public void testWithSpecialCharacters() {
        String json = "[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]";
        assertEquals("{\"[0].abc\\t\":\" \\\" \\r \\t \1234 \"}",
            new JsonFlattener(json).flatten());
        json = "{\" \":[123,\"abc\"]}";
        assertEquals("{\" [0]\":123,\" [1]\":\"abc\"}",
            new JsonFlattener(json).flatten());
    }

    @Test
    public void testWithUnicodeCharacters() {
        String json = "[{\"姓名\":123}]";
        assertEquals("{\"[0].姓名\":123}", new JsonFlattener(json).flatten());
    }

    @Test
    public void testWithFlattenMode() throws IOException {
        String json =Resource.readAsString("test4.json");
        assertEquals(
            "{\"a.b\":1,\"a.c\":null,\"a.d\":[false,{\"i.j\":[false,true,\"xy\"]}],\"e\":\"f\",\"g\":2.3,\"z\":[]}",
            new JsonFlattener(json).withFlattenMode(FlattenMode.KEEP_ARRAYS)
                .flatten());
    }

    @Test
    public void testWithStringEscapePolicyALL() {
        String json = "{\"abc\":{\"def\":\"太極/兩儀\"}}";
        assertEquals("{\"abc.def\":\"\\u592A\\u6975\\/\\u5169\\u5100\"}",
            new JsonFlattener(json).withStringEscapePolicy(StringEscapePolicy.ALL)
                .flatten());
    }

    @Test
    public void testWithStringEscapePolicyALL_BUT_SLASH() {
        String json = "{\"abc\":{\"def\":\"太極/兩儀\"}}";
        assertEquals("{\"abc.def\":\"\\u592A\\u6975/\\u5169\\u5100\"}",
            new JsonFlattener(json)
                .withStringEscapePolicy(StringEscapePolicy.ALL_BUT_SLASH)
                .flatten());
    }

    @Test
    public void testWithStringEscapePolicyALL_BUT_UNICODE() {
        String json = "{\"abc\":{\"def\":\"太極/兩儀\"}}";
        assertEquals("{\"abc.def\":\"太極\\/兩儀\"}", new JsonFlattener(json)
            .withStringEscapePolicy(StringEscapePolicy.ALL_BUT_UNICODE).flatten());
    }

    @Test
    public void testWithStringEscapePolicyALL_BUT_SLASH_AND_UNICODE() {
        String json = "{\"abc\":{\"def\":\"太極/兩儀\"}}";
        assertEquals("{\"abc.def\":\"太極/兩儀\"}", new JsonFlattener(json)
            .withStringEscapePolicy(StringEscapePolicy.ALL_BUT_SLASH_AND_UNICODE)
            .flatten());
    }

    @Test
    public void testWithSeparator() {
        String json = "{\"abc\":{\"def\":123}}";
        assertEquals("{\"abc*def\":123}",
            new JsonFlattener(json).withSeparator('*').flatten());
    }

    @Test
    public void testWithSeparatorException() {
        String json = "{\"abc\":{\"def\":123}}";
        try {
            new JsonFlattener(json).withSeparator('"');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator contains illegal character(\")", e.getMessage());
        }
        try {
            new JsonFlattener(json).withSeparator(' ');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator contains illegal character( )", e.getMessage());
        }
        try {
            new JsonFlattener(json).withSeparator('[');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator([) is already used in brackets", e.getMessage());
        }
        try {
            new JsonFlattener(json).withSeparator(']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator(]) is already used in brackets", e.getMessage());
        }
    }

    @Test
    public void testWithLeftAndRightBracket() {
        String json = "{\"abc\":{\"A.\":[123,\"def\"]}}";
        assertEquals("{\"abc{\\\"A.\\\"}{0}\":123,\"abc{\\\"A.\\\"}{1}\":\"def\"}",
            new JsonFlattener(json).withLeftAndRightBrackets('{', '}').flatten());
    }

    @Test
    public void testWithLeftAndRightBracketsException() {
        String json = "{\"abc\":{\"def\":123}}";
        try {
            new JsonFlattener(json).withLeftAndRightBrackets('#', '#');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Both brackets cannot be the same", e.getMessage());
        }
        try {
            new JsonFlattener(json).withLeftAndRightBrackets('"', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character(\")",
                e.getMessage());
        }
        try {
            new JsonFlattener(json).withLeftAndRightBrackets(' ', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character( )",
                e.getMessage());
        }
        try {
            new JsonFlattener(json).withLeftAndRightBrackets('.', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character(.)",
                e.getMessage());
        }
        try {
            new JsonFlattener(json).withLeftAndRightBrackets('[', '"');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character(\")",
                e.getMessage());
        }
        try {
            new JsonFlattener(json).withLeftAndRightBrackets('[', ' ');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character( )",
                e.getMessage());
        }
        try {
            new JsonFlattener(json).withLeftAndRightBrackets('[', '.');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character(.)",
                e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRootInMap() {
        assertEquals("null", JsonFlattener.flatten("null"));
        assertEquals(null, JsonFlattener.flattenAsMap("null").get("root"));
        assertEquals("123", JsonFlattener.flatten("123"));
        assertEquals(123, JsonFlattener.flattenAsMap("123").get("root"));
        assertEquals("\"abc\"", JsonFlattener.flatten("\"abc\""));
        assertEquals("abc", JsonFlattener.flattenAsMap("\"abc\"").get("root"));
        assertEquals("true", JsonFlattener.flatten("true"));
        assertEquals(true, JsonFlattener.flattenAsMap("true").get("root"));
        assertEquals("[]", JsonFlattener.flatten("[]"));
        assertEquals(Collections.emptyList(),
            JsonFlattener.flattenAsMap("[]").get("root"));
        assertEquals("[[{\"abc.def\":123}]]",
            new JsonFlattener("[[{\"abc\":{\"def\":123}}]]")
                .withFlattenMode(FlattenMode.KEEP_ARRAYS).flatten());
        List<List<Map<String, Object>>> root =
            (List<List<Map<String, Object>>>) new JsonFlattener(
                "[[{\"abc\":{\"def\":123}}]]")
                .withFlattenMode(FlattenMode.KEEP_ARRAYS).flattenAsMap()
                .get("root");
        assertEquals(mapOf("abc.def", 123), root.get(0).get(0));
    }

    @Test
    public void testPrintMode() throws IOException {
        String src = Resource.readAsString("test.json");

        String json =
            new JsonFlattener(src).withPrintMode(PrintMode.MINIMAL).flatten();
        assertEquals(mapper.readTree(json).toString(), json);

        json = new JsonFlattener(src).withPrintMode(PrintMode.PRETTY).flatten();
        assertEquals(mapper.readTree(json).toPrettyString(), json);

        src = "[[123]]";
        json = new JsonFlattener(src).withFlattenMode(FlattenMode.KEEP_ARRAYS)
            .withPrintMode(PrintMode.MINIMAL).flatten();
        assertEquals(mapper.readTree(json).toString(), json);

        json = new JsonFlattener(src).withFlattenMode(FlattenMode.KEEP_ARRAYS)
            .withPrintMode(PrintMode.PRETTY).flatten();
        assertEquals(mapper.readTree(json).toPrettyString(), json);
    }

    @Test
    public void testNoCache() {
        JsonFlattener jf = new JsonFlattener("{\"abc\":{\"def\":123}}");
        assertSame(jf.flattenAsMap(), jf.flattenAsMap());
        assertNotSame(jf.flatten(), jf.flatten());
        assertEquals("{\"abc*def\":123}", jf.withSeparator('*').flatten());
        assertEquals(jf.flatten(), jf.withPrintMode(PrintMode.MINIMAL).flatten());
    }

    @Test
    public void testNullPointerException() {
        try {
            new JsonFlattener("{\"abc\":{\"def\":123}}").withFlattenMode(null);
            fail();
        } catch (NullPointerException e) {
        }
        try {
            new JsonFlattener("{\"abc\":{\"def\":123}}").withStringEscapePolicy(null);
            fail();
        } catch (NullPointerException e) {
        }
        try {
            new JsonFlattener("{\"abc\":{\"def\":123}}").withPrintMode(null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testFlattenWithNestedEmptyJsonObjectAndKeepArraysMode()
        throws IOException {
        String json = Resource.readAsString("test5.json");

        assertEquals(
            "{\"a.b\":1,\"a.c\":null,\"a.d\":[false,{\"i.j\":[false,true]}],\"e\":\"f\",\"g\":2.3,\"z\":{}}",
            new JsonFlattener(json).withFlattenMode(FlattenMode.KEEP_ARRAYS)
                .flatten());
    }

    @Test
    public void testWithSeparatorAndNestedObject() throws IOException {
        String json = Resource.readAsString("test5.json");
        assertEquals(
            "{\"a_b\":1,\"a_c\":null,\"a_d\":[false,{\"i_j\":[false,true]}],\"e\":\"f\",\"g\":2.3,\"z\":{}}",
            new JsonFlattener(json).withFlattenMode(FlattenMode.KEEP_ARRAYS)
                .withSeparator('_').flatten());
    }

    @Test
    public void testWithRootKeyInSourceObject() {
        String json = "{\"" + JsonFlattener.ROOT + "\":null, \"ss\":[123]}";
        assertEquals("{\"" + JsonFlattener.ROOT + "\":null,\"ss[0]\":123}",
            JsonFlattener.flatten(json));
    }

    @Test
    public void testFlattenModeMongodb() throws IOException {
        String src = Resource.readAsString("test_mongo.json");
        String expectedJson = Json.from(Resource.readAsString("test_mongo_flattened.json")).asString();

        String flattened =
            new JsonFlattener(src).withFlattenMode(FlattenMode.MONGO)
                .withPrintMode(PrintMode.MINIMAL).flatten();

        assertEquals(expectedJson, flattened);
    }

    @Test
    public void testFlattenModeMongodbException() {
        String json = "{\"abc\":{\"de.f\":123}}";
        JsonFlattener jf =
            new JsonFlattener(json).withFlattenMode(FlattenMode.MONGO);
        try {
            jf.flatten();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Key cannot contain separator(.) in FlattenMode.MONGO",
                e.getMessage());
        }
    }

    @Test
    public void testWithKeyTransformer() {
        String json = "{\"abc\":{\"de.f\":123}}";
        JsonFlattener jf =
            new JsonFlattener(json).withFlattenMode(FlattenMode.MONGO)
                .withKeyTransformer(new KeyTransformer() {

                    @Override
                    public String transform(String key) {
                        return key.replace('.', '_');
                    }

                });
        assertEquals("{\"abc.de_f\":123}", jf.flatten());
    }

    @Test
    public void testWithFlattenModeKeepBottomArrays() throws IOException {
        String json = Resource.readAsString("test_keep_primitive_arrays.json");
        String expectedJson = Json.from(Resource.readAsString("test_keep_primitive_arrays_flattened.json")).toString();

        JsonFlattener jf = new JsonFlattener(json)
            .withFlattenMode(FlattenMode.KEEP_PRIMITIVE_ARRAYS)
            .withPrintMode(PrintMode.MINIMAL);
        String flattened = jf.flatten();

        assertEquals(expectedJson, flattened);
    }

    @Test
    public void testWithIgnoreReservedCharacters() {
        String json = "{\"matrix\":{\"agent.smith\":\"1999\"}}";

        assertEquals("{\"matrix[\\\"agent.smith\\\"]\":\"1999\"}",
            JsonFlattener.flatten(json));
        assertEquals("{\"matrix.agent.smith\":\"1999\"}",
            new JsonFlattener(json).ignoreReservedCharacters().flatten());

        assertThrows(IllegalArgumentException.class, () -> {
            new JsonFlattener(json).withFlattenMode(FlattenMode.MONGO).flatten();
        });
        assertEquals("{\"matrix.agent.smith\":\"1999\"}",
            new JsonFlattener(json).withFlattenMode(FlattenMode.MONGO)
                .ignoreReservedCharacters().flatten());

        String jsonArray =
            "[{\"matrix\":\"reloaded\",\"agent\":{\"smith_no\":\"1\"}},"
                + "{\"matrix\":\"reloaded\",\"agent\":{\"smith_no\":\"2\"}}]";

        assertEquals(
            "{\"_0_matrix\":\"reloaded\",\"_0_agent_smith_no\":\"1\",\"_1_matrix\":\"reloaded\",\"_1_agent_smith_no\":\"2\"}",
            new JsonFlattener(jsonArray).withFlattenMode(FlattenMode.MONGO)
                .withSeparator('_').ignoreReservedCharacters().flatten());
        assertEquals(
            "[{\"matrix\":\"reloaded\",\"agent_smith_no\":\"1\"},{\"matrix\":\"reloaded\",\"agent_smith_no\":\"2\"}]",
            new JsonFlattener(jsonArray).withFlattenMode(FlattenMode.KEEP_ARRAYS)
                .withSeparator('_').ignoreReservedCharacters().flatten());
    }

    private <K,V> Map<K,V> mapOf(K k, V v) {
        return new HashMap<K,V>() {{
           put(k, v); 
        }};
    }
}
