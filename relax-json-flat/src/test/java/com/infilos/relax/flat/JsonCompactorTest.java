package com.infilos.relax.flat;

import java.io.IOException;
import java.util.Map;

import com.infilos.relax.*;
import com.infilos.utils.Resource;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.wnameless.json.base.JacksonJsonCore;
//import com.github.wnameless.json.flattener.FlattenMode;
//import com.github.wnameless.json.flattener.JsonFlattener;
//import com.github.wnameless.json.flattener.KeyTransformer;
//import com.github.wnameless.json.flattener.PrintMode;
//import com.google.common.base.Charsets;
//import com.google.common.io.Resources;
import org.junit.Test;

public class JsonCompactorTest extends Assert {

    ObjectMapper mapper = new ObjectMapper();

    private Map<String, ?> toMap(String json)
        throws JsonMappingException, JsonProcessingException {
        return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }

    private Map<String, ?> toRootMap(String json)
        throws JsonMappingException, JsonProcessingException {
        return mapper.readValue("{\"" + JsonCompactor.ROOT + "\":" + json + "}",
            new TypeReference<Map<String, Object>>() {
            });
    }

    @Test
    public void testUnflatten()
        throws JsonMappingException, JsonProcessingException {
        assertEquals(
            "{\"a\":{\"b\":1,\"c\":null,\"d\":[false,true,{\"sss\":777,\"vvv\":888}]},\"e\":\"f\",\"g\":2.3}",
            JsonCompactor.compact(
                "{\"a.b\":1,\"a.c\":null,\"a.d[1]\":true,\"a.d[0]\":false,\"a.d[2].sss\":777,\"a.d[2].vvv\":888,\"e\":\"f\",\"g\":2.3}"));

        assertEquals("[1,[2,3],4,{\"abc\":5}]", JsonCompactor.compact(
            "{\"[1][0]\":2,\"[0]\":1,\"[1][1]\":3,\"[2]\":4,\"[3].abc\":5}"));

        assertEquals("{\" \\\"abc\":{\"def \":123}}", JsonCompactor
            .compact(JsonFlattener.flatten("{\" \\\"abc\":{\"def \":123}}")));

        assertEquals("{\" ].$f\":{\"abc\":{\"def\":[123]}}}",
            JsonCompactor.compact("{\"[\\\" ].$f\\\"].abc.def[0]\":123}"));

        assertEquals("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]",
            JsonCompactor.compact(
                JsonFlattener.flatten("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]")));

        // Map
        assertEquals(
            "{\"a\":{\"b\":1,\"c\":null,\"d\":[false,true,{\"sss\":777,\"vvv\":888}]},\"e\":\"f\",\"g\":2.3}",
            JsonCompactor.compact(toMap(
                "{\"a.b\":1,\"a.c\":null,\"a.d[1]\":true,\"a.d[0]\":false,\"a.d[2].sss\":777,\"a.d[2].vvv\":888,\"e\":\"f\",\"g\":2.3}")));

        assertEquals("[1,[2,3],4,{\"abc\":5}]", JsonCompactor.compact(toMap(
            "{\"[1][0]\":2,\"[0]\":1,\"[1][1]\":3,\"[2]\":4,\"[3].abc\":5}")));

        assertEquals("{\" \\\"abc\":{\"def \":123}}", JsonCompactor.compact(
            toMap(JsonFlattener.flatten("{\" \\\"abc\":{\"def \":123}}"))));

        assertEquals("{\" ].$f\":{\"abc\":{\"def\":[123]}}}", JsonCompactor
            .compact(toMap("{\"[\\\" ].$f\\\"].abc.def[0]\":123}")));

        assertEquals("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]",
            JsonCompactor.compact(toMap(
                JsonFlattener.flatten("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]"))));
    }

    @Test
    public void testUnflattenAsMap()
        throws JsonMappingException, JsonProcessingException {
        assertEquals(toMap(
                "{\"a\":{\"b\":1,\"c\":null,\"d\":[false,true,{\"sss\":777,\"vvv\":888}]},\"e\":\"f\",\"g\":2.3}"),
            JsonCompactor.compactAsMap(
                "{\"a.b\":1,\"a.c\":null,\"a.d[1]\":true,\"a.d[0]\":false,\"a.d[2].sss\":777,\"a.d[2].vvv\":888,\"e\":\"f\",\"g\":2.3}"));

        assertEquals(toRootMap("[1,[2,3],4,{\"abc\":5}]"),
            JsonCompactor.compactAsMap(
                "{\"[1][0]\":2,\"[0]\":1,\"[1][1]\":3,\"[2]\":4,\"[3].abc\":5}"));

        assertEquals(toMap("{\" \\\"abc\":{\"def \":123}}"),
            JsonCompactor.compactAsMap(
                JsonFlattener.flatten("{\" \\\"abc\":{\"def \":123}}")));

        assertEquals(toMap("{\" ].$f\":{\"abc\":{\"def\":[123]}}}"),
            JsonCompactor.compactAsMap("{\"[\\\" ].$f\\\"].abc.def[0]\":123}"));

        assertEquals(toRootMap("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]"),
            JsonCompactor.compactAsMap(
                JsonFlattener.flatten("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]")));

        // Map
        assertEquals(toMap(
                "{\"a\":{\"b\":1,\"c\":null,\"d\":[false,true,{\"sss\":777,\"vvv\":888}]},\"e\":\"f\",\"g\":2.3}"),
            JsonCompactor.compactAsMap(toMap(
                "{\"a.b\":1,\"a.c\":null,\"a.d[1]\":true,\"a.d[0]\":false,\"a.d[2].sss\":777,\"a.d[2].vvv\":888,\"e\":\"f\",\"g\":2.3}")));

        assertEquals(toRootMap("[1,[2,3],4,{\"abc\":5}]"),
            JsonCompactor.compactAsMap(toMap(
                "{\"[1][0]\":2,\"[0]\":1,\"[1][1]\":3,\"[2]\":4,\"[3].abc\":5}")));

        assertEquals(toMap("{\" \\\"abc\":{\"def \":123}}"),
            JsonCompactor.compactAsMap(
                toMap(JsonFlattener.flatten("{\" \\\"abc\":{\"def \":123}}"))));

        assertEquals(toMap("{\" ].$f\":{\"abc\":{\"def\":[123]}}}"), JsonCompactor
            .compactAsMap(toMap("{\"[\\\" ].$f\\\"].abc.def[0]\":123}")));

        assertEquals(toRootMap("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]"),
            JsonCompactor.compactAsMap(toMap(
                JsonFlattener.flatten("[{\"abc\\t\":\" \\\" \\r \\t \1234 \"}]"))));
    }

    @Test
    public void testUnflattenWithArrayOfNestedObjectsInValByKeepArraysMode()
        throws IOException {
        String json = Resource.readAsString("test6.json");

        String flattendJson = new JsonFlattener(json)
            .withFlattenMode(FlattenMode.KEEP_ARRAYS).flatten();
        assertEquals("{\"a\":[1,2,3],\"b\":[{\"c.d\":[1,2]}]}", flattendJson);
        assertEquals("{\"a\":[1,2,3],\"b\":[{\"c\":{\"d\":[1,2]}}]}",
            JsonCompactor.compact(flattendJson));

        // Map
        Map<String, Object> flattendMap = new JsonFlattener(json)
            .withFlattenMode(FlattenMode.KEEP_ARRAYS).flattenAsMap();
        assertEquals("{\"a\":[1,2,3],\"b\":[{\"c.d\":[1,2]}]}",
            flattendMap.toString());
        assertEquals("{\"a\":[1,2,3],\"b\":[{\"c\":{\"d\":[1,2]}}]}",
            JsonCompactor.compact(flattendMap));
    }

    @Test
    public void testUnflattenWithKeyContainsDotAndSquareBracket()
        throws JsonMappingException, JsonProcessingException {
        assertEquals("[1,[2,3],4,{\"ab.c.[\":5}]", JsonCompactor.compact(
            "{\"[1][0]\":2,\"[ 0 ]\":1,\"[1][1]\":3,\"[2]\":4,\"[3][ \\\"ab.c.[\\\" ]\":5}"));

        // Map
        assertEquals("[1,[2,3],4,{\"ab.c.[\":5}]", JsonCompactor.compact(toMap(
            "{\"[1][0]\":2,\"[ 0 ]\":1,\"[1][1]\":3,\"[2]\":4,\"[3][ \\\"ab.c.[\\\" ]\":5}")));
    }

    @Test
    public void testUnflattenWithReversedIndexesWithinObjects()
        throws IOException {
        String json = Resource.readAsString("test3.json");

        assertEquals("{\"List\":[{\"type\":\"A\"},null,{\"type\":\"B\"}]}",
            JsonCompactor.compact(json));

        // Map
        assertEquals("{\"List\":[{\"type\":\"A\"},null,{\"type\":\"B\"}]}",
            JsonCompactor.compact(toMap(json)));
    }

    @Test
    public void testUnflattenWithReversedIndexes()
        throws JsonMappingException, JsonProcessingException {
        String json = "{\"[1][1]\":\"B\",\"[0][0]\":\"A\"}";

        assertEquals("[[\"A\"],[null,\"B\"]]", JsonCompactor.compact(json));

        // Map
        assertEquals("[[\"A\"],[null,\"B\"]]",
            JsonCompactor.compact(toMap(json)));
    }

    @Test
    public void testUnflattenWithInitComplexKey()
        throws JsonMappingException, JsonProcessingException {
        String json = "{\"[\\\"b.b\\\"].aaa\":123}";

        assertEquals("{\"b.b\":{\"aaa\":123}}", JsonCompactor.compact(json));

        // Map
        assertEquals("{\"b.b\":{\"aaa\":123}}",
            JsonCompactor.compact(toMap(json)));
    }

    @Test
    public void testHashCode() throws IOException {
        String json1 = "[[123]]";
        String json2 = "[[[123]]]";

        JsonCompactor unflattener = new JsonCompactor(json1);
        assertEquals(unflattener.hashCode(), unflattener.hashCode());
        assertEquals(unflattener.hashCode(), new JsonCompactor(json1).hashCode());
        assertNotEquals(unflattener.hashCode(),
            new JsonCompactor(json2).hashCode());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEquals() throws IOException {
        String json1 = "[[123]]";
        String json2 = "[[[123]]]";

        JsonCompactor unflattener = new JsonCompactor(json1);
        assertTrue(unflattener.equals(unflattener));
        assertTrue(unflattener.equals(new JsonCompactor(json1)));
        assertFalse(unflattener.equals(new JsonCompactor(json2)));
        assertFalse(unflattener.equals(123L));
    }

    @Test
    public void testToString() throws IOException {
        String json = "[[123]]";

        assertEquals("JsonCompactor{root=[[123]]}",
            new JsonCompactor(json).toString());
    }

    @Test
    public void testWithKeepArrays() throws IOException {
        String json = Json.from(Resource.readAsString("test4.json")).asString();

        assertEquals(json, JsonCompactor.compact(new JsonFlattener(json)
            .withFlattenMode(FlattenMode.KEEP_ARRAYS).flatten()));

        // Map
        assertEquals(json, JsonCompactor.compact(toMap(new JsonFlattener(json)
            .withFlattenMode(FlattenMode.KEEP_ARRAYS).flatten())));
    }

    @Test
    public void testWithSeparater()
        throws JsonMappingException, JsonProcessingException {
        String json = "{\"abc\":{\"def\":123}}";
        assertEquals(json,
            new JsonCompactor(
                new JsonFlattener(json).withSeparator('*').flatten())
                .withSeparator('*').compact());

        // Map
        assertEquals(json,
            new JsonCompactor(
                toMap(new JsonFlattener(json).withSeparator('*').flatten()))
                .withSeparator('*').compact());
    }

    @Test
    public void testWithSeparaterException()
        throws JsonMappingException, JsonProcessingException {
        String json = "{\"abc\":{\"def\":123}}";
        try {
            new JsonCompactor(json).withSeparator('"');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator contains illegal character(\")", e.getMessage());
        }
        try {
            new JsonCompactor(json).withSeparator(' ');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator contains illegal character( )", e.getMessage());
        }
        try {
            new JsonCompactor(json).withSeparator('[');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator([) is already used in brackets", e.getMessage());
        }
        try {
            new JsonCompactor(json).withSeparator(']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator(]) is already used in brackets", e.getMessage());
        }

        // Map
        try {
            new JsonCompactor(toMap(json)).withSeparator('"');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator contains illegal character(\")", e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withSeparator(' ');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator contains illegal character( )", e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withSeparator('[');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator([) is already used in brackets", e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withSeparator(']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Separator(]) is already used in brackets", e.getMessage());
        }
    }

    @Test
    public void testWithLeftAndRightBrackets()
        throws JsonMappingException, JsonProcessingException {
        String json = "{\"abc[\\\"A.\\\"][0]\":123}";
        assertEquals("{\"abc\":{\"A.\":[123]}}", new JsonCompactor(json)
            .withLeftAndRightBrackets('[', ']').compact());

        json = "{\"abc{\\\"A.\\\"}{0}\":123}";
        assertEquals("{\"abc\":{\"A.\":[123]}}", new JsonCompactor(json)
            .withLeftAndRightBrackets('{', '}').compact());

        // Map
        json = "{\"abc[\\\"A.\\\"][0]\":123}";
        assertEquals("{\"abc\":{\"A.\":[123]}}", new JsonCompactor(toMap(json))
            .withLeftAndRightBrackets('[', ']').compact());

        json = "{\"abc{\\\"A.\\\"}{0}\":123}";
        assertEquals("{\"abc\":{\"A.\":[123]}}", new JsonCompactor(toMap(json))
            .withLeftAndRightBrackets('{', '}').compact());
    }

    @Test
    public void testWithLeftAndRightBracketsException()
        throws JsonMappingException, JsonProcessingException {
        String json = "{\"abc\":{\"def\":123}}";
        try {
            new JsonCompactor(json).withLeftAndRightBrackets('#', '#');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Both brackets cannot be the same", e.getMessage());
        }
        try {
            new JsonCompactor(json).withLeftAndRightBrackets('"', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character(\")",
                e.getMessage());
        }
        try {
            new JsonCompactor(json).withLeftAndRightBrackets(' ', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character( )",
                e.getMessage());
        }
        try {
            new JsonCompactor(json).withLeftAndRightBrackets('.', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character(.)",
                e.getMessage());
        }
        try {
            new JsonCompactor(json).withLeftAndRightBrackets('[', '"');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character(\")",
                e.getMessage());
        }
        try {
            new JsonCompactor(json).withLeftAndRightBrackets('[', ' ');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character( )",
                e.getMessage());
        }
        try {
            new JsonCompactor(json).withLeftAndRightBrackets('[', '.');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character(.)",
                e.getMessage());
        }

        // Map
        try {
            new JsonCompactor(toMap(json)).withLeftAndRightBrackets('#', '#');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Both brackets cannot be the same", e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withLeftAndRightBrackets('"', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character(\")",
                e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withLeftAndRightBrackets(' ', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character( )",
                e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withLeftAndRightBrackets('.', ']');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Left bracket contains illegal character(.)",
                e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withLeftAndRightBrackets('[', '"');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character(\")",
                e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withLeftAndRightBrackets('[', ' ');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character( )",
                e.getMessage());
        }
        try {
            new JsonCompactor(toMap(json)).withLeftAndRightBrackets('[', '.');
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Right bracket contains illegal character(.)",
                e.getMessage());
        }
    }

    @Test
    public void testWithNonObject()
        throws JsonMappingException, JsonProcessingException {
        assertEquals("123", JsonCompactor.compact("123"));
        assertEquals("\"abc\"", JsonCompactor.compact("\"abc\""));
        assertEquals("true", JsonCompactor.compact("true"));
        assertEquals("[1,2,3]", JsonCompactor.compact("[1,2,3]"));
    }

    @Test
    public void testWithNestedArrays() {
        assertEquals("[[{\"abc\":{\"def\":123}}]]",
            JsonCompactor.compact("[[{\"abc.def\":123}]]"));
    }

    @Test
    public void testPrintMode() throws IOException {
        String src = "{\"abc.def\":123}";
        String json =
            new JsonCompactor(src).withPrintMode(PrintMode.MINIMAL).compact();
        assertEquals(mapper.readTree(json).toString(), json);

        json = new JsonCompactor(src).withPrintMode(PrintMode.PRETTY).compact();
        assertEquals(mapper.readTree(json).toPrettyString(), json);
    }

    @Test
    public void testNoCache()
        throws JsonMappingException, JsonProcessingException {
        JsonCompactor ju = new JsonCompactor("{\"abc.def\":123}");
        assertNotSame(ju.compact(), ju.compact());
        assertEquals("{\"abc\":{\"def\":123}}",
            ju.withPrintMode(PrintMode.MINIMAL).compact());

        // Map
        ju = new JsonCompactor(toMap("{\"abc.def\":123}"));
        assertNotSame(ju.compact(), ju.compact());
        assertEquals("{\"abc\":{\"def\":123}}",
            ju.withPrintMode(PrintMode.MINIMAL).compact());
    }

    @Test
    public void testNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            new JsonCompactor("{\"abc.def\":123}").withPrintMode(null);
        });
    }

    //@Test
    //public void testInitByReader() throws IOException {
    //    StringReader sr = new StringReader("{\"abc.def\":123}");
    //
    //    assertEquals(new JsonUnflattener("{\"abc.def\":123}"),
    //        new JsonUnflattener(sr));
    //}

    @Test
    public void testInitByMap()
        throws JsonMappingException, JsonProcessingException {
        assertEquals(new JsonCompactor("{\"abc.def\":123}"),
            new JsonCompactor(toMap("{\"abc.def\":123}")));
    }

    @Test
    public void testFlattenModeMongodb() throws IOException {
        String expectedJson = Resource.readAsString("test_mongo.json");
        String json = Resource.readAsString("test_mongo_flattened.json");

        JsonCompactor ju =
            new JsonCompactor(json).withFlattenMode(FlattenMode.MONGO);
        assertEquals(mapper.readTree(expectedJson).toString(), ju.compact());

        // Map
        ju = new JsonCompactor(toMap(json)).withFlattenMode(FlattenMode.MONGO);
        assertEquals(mapper.readTree(expectedJson).toString(), ju.compact());
    }

    @Test
    public void testWithKeyTransformer()
        throws JsonMappingException, JsonProcessingException {
        String json = "{\"abc.de_f\":123}";
        JsonCompactor ju =
            new JsonCompactor(json).withFlattenMode(FlattenMode.MONGO)
                .withKeyTransformer(new KeyTransformer() {

                    @Override
                    public String transform(String key) {
                        return key.replace('_', '.');
                    }

                });
        assertEquals("{\"abc\":{\"de.f\":123}}", ju.compact());

        // Map
        ju = new JsonCompactor(toMap(json)).withFlattenMode(FlattenMode.MONGO)
            .withKeyTransformer(new KeyTransformer() {

                @Override
                public String transform(String key) {
                    return key.replace('_', '.');
                }

            });
        assertEquals("{\"abc\":{\"de.f\":123}}", ju.compact());
    }

    @Test
    public void testWithFlattenModeKeepBottomArrays() throws IOException {
        String expectedJson = Resource.readAsString("test_keep_primitive_arrays.json");
        String json = Resource.readAsString("test_keep_primitive_arrays_flattened.json");

        JsonCompactor ju = new JsonCompactor(json)
            .withFlattenMode(FlattenMode.KEEP_PRIMITIVE_ARRAYS);
        assertEquals(mapper.readTree(expectedJson).toString(), ju.compact());

        // Map
        ju = new JsonCompactor(toMap(json))
            .withFlattenMode(FlattenMode.KEEP_PRIMITIVE_ARRAYS);
        assertEquals(mapper.readTree(expectedJson).toString(), ju.compact());
    }
}
