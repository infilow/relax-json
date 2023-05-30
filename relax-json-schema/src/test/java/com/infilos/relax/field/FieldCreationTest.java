package com.infilos.relax.field;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

/**
 *
 * 
 */
class FieldCreationTest extends Assert {

    @Test
    //@DisplayName("Create AnyField")
    void testAnyFieldCreation() throws Exception{
        Field testField = new AnyField("anon");
        assertEquals(testField.getName(), "anon");
        assertEquals(testField.getType(), "any");
    }

    @Test
    //@DisplayName("Create AnyField, full constructor")
    void testAnyFieldCreation2() throws Exception{
        Field testField = new AnyField("anon", Field.FIELD_FORMAT_DEFAULT, "title", "descriptions",
                new URI("https://github.com"), null, null);
        assertEquals( "anon", testField.getName());
        assertEquals( "any", testField.getType());
        assertEquals("title", testField.getTitle());
        assertEquals("descriptions", testField.getDescription());
        assertEquals(new URI("https://github.com"), testField.getRdfType());
        assertNull(testField.getConstraints());
        assertNull(testField.getOptions());
    }

    @Test
    //@DisplayName("Create AnyField from JSON")
    void testAnyFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"anon\",\"format\":\"\",\"description\":\"\"," +
                "\"title\":\"\",\"type\":\"any\",\"constraints\":{}}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "anon");
        assertEquals(testField.getType(), "any");
    }

    @Test
    //@DisplayName("Create ArrayField")
    void testArrayFieldCreation() throws Exception{
        Field testField = new ArrayField("employees");
        assertEquals(testField.getName(), "employees");
        assertEquals(testField.getType(), "array");
    }

    @Test
    //@DisplayName("Create ArrayField from JSON")
    void testArrayFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"employees\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"array\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "employees");
        assertEquals(testField.getType(), "array");
    }

    @Test
    //@DisplayName("Create BooleanField")
    void testBooleanFieldCreation() throws Exception{
        Field testField = new BooleanField("is_valid");
        assertEquals(testField.getName(), "is_valid");
        assertEquals(testField.getType(), "boolean");
    }

    @Test
    //@DisplayName("Create BooleanField from JSON")
    void testBooleanFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"is_valid\",\"format\":\"default\",\"description\":\"\"" +
                ",\"type\":\"boolean\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "is_valid");
        assertEquals(testField.getType(), "boolean");
    }

    @Test
    //@DisplayName("Create DateField")
    void testDateFieldCreation() throws Exception{
        String fieldName = "today";
        Field testField = new DateField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "date");
    }

    @Test
    //@DisplayName("Create DateField from JSON")
    void testDateFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"today\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"date\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "today");
        assertEquals(testField.getType(), "date");
    }

    @Test
    //@DisplayName("Create DatetimeField")
    void testDatetimeFieldCreation() throws Exception{
        String fieldName = "today_noon";
        Field testField = new DatetimeField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "datetime");
    }

    @Test
    //@DisplayName("Create DatetimeField from JSON")
    void testDatetimeieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"today_noon\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"datetime\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "today_noon");
        assertEquals(testField.getType(), "datetime");
    }

    @Test
    //@DisplayName("Create DurationField")
    void testDurationFieldCreation() throws Exception{
        String fieldName = "aday";
        Field testField = new DurationField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "duration");
    }

    @Test
    //@DisplayName("Create DatetimeField from JSON")
    void testDurationFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"aday\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"duration\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "aday");
        assertEquals(testField.getType(), "duration");
    }

    @Test
    //@DisplayName("Create GeojsonField")
    void testGeojsonFieldCreation() throws Exception{
        String fieldName = "latlong";
        Field testField = new GeojsonField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "geojson");
    }

    @Test
    //@DisplayName("Create GeojsonField from JSON")
    void testGeojsonFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"latlong\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"geojson\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "latlong");
        assertEquals(testField.getType(), "geojson");
    }

    @Test
    //@DisplayName("Create GeopointField")
    void testGeopointFieldCreation() throws Exception{
        String fieldName = "latlong";
        Field testField = new GeopointField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "geopoint");
    }

    @Test
    //@DisplayName("Create GeopointField from JSON")
    void testGeopointFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"latlong\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"geopoint\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "latlong");
        assertEquals(testField.getType(), "geopoint");
    }

    @Test
    //@DisplayName("Create IntegerField")
    void testIntegerFieldCreation() throws Exception{
        String fieldName = "int";
        Field testField = new IntegerField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "integer");
    }

    @Test
    //@DisplayName("Create IntegerField from JSON")
    void testIntegerFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"int\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"integer\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "int");
        assertEquals(testField.getType(), "integer");
    }

    @Test
    //@DisplayName("Create NumberField")
    void testNumberFieldCreation() throws Exception{
        String fieldName = "number";
        Field testField = new NumberField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "number");
    }

    @Test
    //@DisplayName("Create NumberField from JSON")
    void testNumberFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"number\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"number\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "number");
        assertEquals(testField.getType(), "number");
    }

    @Test
    //@DisplayName("Create ObjectField")
    void testObjectFieldCreation() throws Exception{
        String fieldName = "obj";
        Field testField = new ObjectField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "object");
    }

    @Test
    //@DisplayName("Create ObjectField from JSON")
    void testObjectFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"obj\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"object\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "obj");
        assertEquals(testField.getType(), "object");
    }

    @Test
    //@DisplayName("Create StringField")
    void testStringFieldCreation() throws Exception{
        Field testField = new StringField("city");
        assertEquals(testField.getName(), "city");
        assertEquals(testField.getType(), "string");
    }

    @Test
    //@DisplayName("Create StringField from JSON")
    void testStringFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"city\",\"format\":\"\",\"description\":\"\"," +
                "\"title\":\"\",\"type\":\"string\",\"constraints\":{}}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "city");
        assertEquals(testField.getType(), "string");
    }

    @Test
    //@DisplayName("Create TimeField")
    void testTimeFieldCreation() throws Exception{
        String fieldName = "noon";
        Field testField = new TimeField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "time");
    }

    @Test
    //@DisplayName("Create TimeField from JSON")
    void testTimeFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"noon\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"time\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "noon");
        assertEquals(testField.getType(), "time");
    }

    @Test
    //@DisplayName("Create YearField")
    void testYearFieldCreation() throws Exception{
        String fieldName = "1997";
        Field testField = new YearField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "year");
    }

    @Test
    //@DisplayName("Create YearField from JSON")
    void testYearFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"1997\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"year\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "1997");
        assertEquals(testField.getType(), "year");
    }

    @Test
    //@DisplayName("Create YearmonthField")
    void testYearmonthFieldFieldCreation() throws Exception{
        String fieldName = "dec1997";
        Field testField = new YearmonthField(fieldName);
        assertEquals(testField.getName(), fieldName);
        assertEquals(testField.getType(), "yearmonth");
    }

    @Test
    //@DisplayName("Create YearmonthField from JSON")
    void testYearmonthFieldFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"dec1997\",\"format\":\"default\",\"description\":\"\"" +
                ",\"type\":\"yearmonth\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertEquals(testField.getName(), "dec1997");
        assertEquals(testField.getType(), "yearmonth");
    }

    @Test
    //@DisplayName("Test undefined Field type creation")
    void testUndefinedFieldCreation() throws Exception{
        String type = "anon";
        String name = "anon";
        Field testField = Field.forType(type, name);
        assertTrue(testField instanceof AnyField);
        assertEquals(testField.getName(), "anon");
        assertEquals(testField.getType(), "any");
    }

    @Test
    //@DisplayName("Test undefined Field type creation from JSON")
    void testUndefinedFieldFieldCreationFromString() throws Exception{
        String testJson = "{\"name\":\"anon\",\"format\":\"default\",\"description\":\"\"," +
                "\"type\":\"anon\",\"title\":\"\"}";
        Field testField = Field.fromJson(testJson);
        assertTrue(testField instanceof AnyField);
        assertEquals("anon", testField.getName());
        assertEquals("any", testField.getType());
    }


    @Test
    //@DisplayName("Test similar() method")
    void testSimilar() throws Exception{
        Field testField1 = new BooleanField("is_valid");
        Field testField2 = new BooleanField("is_valid");
        Field testField3 = new BooleanField(null);
        Field testField4 = new BooleanField("");
        Field testField5 = new BooleanField("also_valid");
        Field testField6 = new YearField("is_valid");
        assertTrue(testField1.similar(testField2));
        assertTrue(testField3.similar(testField4));
        assertFalse(testField1.similar(testField3));
        assertFalse(testField1.similar(testField4));
        assertFalse(testField1.similar(testField5));
        assertFalse(testField3.similar(testField1));
        assertFalse(testField4.similar(testField1));
        assertFalse(testField4.similar(testField5));
        assertFalse(testField1.similar(testField6));
    }

    // spec: https://frictionlessdata.io/specs/table-schema/#rich-types
    @Test
    //@DisplayName("Test fix for Issue https://github.com/frictionlessdata/tableschema-java/issues/21")
    void testIssue22() {
        String testJson =
                "        {\n" +
                "          \"name\": \"Country\",\n" +
                "          \"type\": \"string\",\n" +
                "          \"rdfType\": \"http://schema.org/Country\"\n" +
                "        }\n";
        Field testField = Field.fromJson(testJson);
        assertEquals( "Country", testField.getName());
        assertEquals("string", testField.getType());
        assertEquals("http://schema.org/Country", testField.getRdfType().toString());
    }

}
