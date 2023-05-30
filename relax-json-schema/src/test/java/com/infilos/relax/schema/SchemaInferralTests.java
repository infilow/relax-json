package com.infilos.relax.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infilos.relax.*;
import org.junit.Test;

import java.io.File;

import static com.infilos.relax.TestHelper.getResourceFile;
import static org.junit.Assert.assertEquals;

public class SchemaInferralTests {

    @Test
    //@DisplayName("Infer a Schema and test MapValueComparator")
    public void inferASchema() throws Exception{
        File basePath = getResourceFile("/fixtures/data/");
        File source = getResourceFile("employee_data.csv");
        Table table = Table.fromSource(source, basePath);

        Schema schema = table.inferSchema();

        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(schema.toJsonString(), Object.class);
        String expectedString = TestHelper.getResourceFileContent(
                "/fixtures/schema/employee_schema.json");
        assertEquals(objectMapper.readValue(expectedString, Object.class), jsonObject);
    }
}
