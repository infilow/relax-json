package com.infilos.relax.source;

import com.infilos.relax.TestHelper;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

class JsonArrayDataSourceFormatTest extends Assert {
    private String populationJson = "[" +
            "{" +
            "\"city\": \"london\"," +
            "\"year\": 2017," +
            "\"population\": 8780000" +
            "}," +
            "{" +
            "\"city\": \"paris\"," +
            "\"year\": 2017," +
            "\"population\": 2240000" +
            "}," +
            "{" +
            "\"city\": \"rome\"," +
            "\"year\": 2017," +
            "\"population\": 2860000" +
            "}" +
            "]";

    private String populationJsonMissingEntry = "[" +
            "{" +
            "\"city\": \"london\"," +
            "\"population\": 8780000" +
            "}," +
            "{" +
            "\"city\": \"paris\"," +
            "\"year\": 2017," +
            "\"population\": 2240000" +
            "}," +
            "{" +
            "\"city\": \"rome\"," +
            "\"year\": 2017," +
            "\"population\": 2860000" +
            "}" +
            "]";

    private final String[] populationHeaders = new String[]{
            "city", "year", "population"
    };

    @Test
    //@DisplayName("Test DataSourceFormat.createDataSourceFormat creates JsonArrayDataSourceFormat from " +
    //        "JSON array data")
    void testCreateJsonArrayDataSource() throws Exception{
        DataSource ds = DataSource.create(populationJson);
        assertTrue(ds instanceof JsonArrayDataSource);
    }

    @Test
    //@DisplayName("Validate Header extraction from a JsonArrayDataSourceFormat")
    void testJsonArrayDataSourceHeaders() throws Exception{
        DataSource ds = DataSource.create(populationJson);
        String[] headers = ds.headers();
        assertArrayEquals(populationHeaders, headers);
    }

    @Test
    //@DisplayName("Validate creating a JsonArrayDataSourceFormat from JSON file")
    void testSafePathCreationJson() throws Exception {
        DataSource ds = DataSource.create(new File("simple_geojson.json"),
                TestHelper.getTestDataDirectory());
        assertNotNull(ds);
    }
/*
    @Test
    //@DisplayName("Validate creating and writing a JsonArrayDataSourceFormat from JSON with null entries")
    void testCreateAndWriteJsonArrayDataSourceWithMissingEntries() throws Exception {
        DataSourceFormat ds = DataSourceFormat.createDataSourceFormat(populationJsonMissingEntry);

        File tempFile = Files.createTempFile("tableschema-", ".json").toFile();
        try (FileWriter wr = new FileWriter(tempFile);
                BufferedWriter bwr = new BufferedWriter(wr)) {
            ds.writeCsv(bwr, null, populationHeaders);
        }
    }

    @Test
    //@DisplayName("Validate creating and writing a JsonArrayDataSourceFormat from JSON without headers " +
            "raises an exception")
    void testCreateAndWriteJsonArrayDataSourceWithoutHeaders() throws Exception {
        DataSourceFormat ds = DataSourceFormat.createDataSourceFormat(populationJsonMissingEntry);

        File tempFile = Files.createTempFile("tableschema-", ".json").toFile();
        Assertions.assertThrows(Exception.class, () -> {
        try (FileWriter wr = new FileWriter(tempFile);
             BufferedWriter bwr = new BufferedWriter(wr)) {
            ds.writeCsv(bwr, null, null);
        }
        });
    }
*/
    @Test
    //@DisplayName("Validate creating a JsonArrayDataSourceFormat from InputStream containing JSON")
    void testSafePathInputStreamCreationJson() throws Exception {
        DataSource ds;
        File inFile = new File(TestHelper.getTestDataDirectory(), "data/population.json");
        try (FileInputStream is = new FileInputStream(inFile)) {
            ds = new JsonArrayDataSource(is);
            assertArrayEquals(populationHeaders, ds.headers());
        }
        assertNotNull(ds);
    }

    @Test
    //@DisplayName("Validate creating a JsonArrayDataSourceFormat from wrong input data (CSV) raises" +
    //        "an exception")
    void testWrongInputStreamCreationJson() throws Exception {
        File inFile = new File(TestHelper.getTestDataDirectory(), "data/population.csv");
        assertThrows(Exception.class, () -> {
            try (FileInputStream is = new FileInputStream(inFile)) {
                DataSource ds = new JsonArrayDataSource(is);
                assertArrayEquals(populationHeaders, ds.headers());
            }
        });
    }

/*
    @Test
    void writeJsonToFile() throws Exception{
        String content = null;
        String popCsv;

        File inFile = new File(TestHelper.getTestDataDirectory(), "data/population.json");
        popCsv = new String(Files.readAllBytes(inFile.toPath()));
        DataSourceFormat ds = DataSourceFormat.createDataSourceFormat(popCsv);

        File tempFile = Files.createTempFile("tableschema-", ".json").toFile();
        ds.write(tempFile);
        try (FileReader fr = new FileReader(tempFile)) {
            try (BufferedReader rdr = new BufferedReader(fr)) {
                content = rdr.lines().collect(Collectors.joining("\n"));
            }
        }
        // evade the CRLF mess by nuking all CR chars
        Assertions.assertEquals(content.replaceAll("[\\r\\n ]", ""),
                popCsv.replaceAll("[\\r\\n ]", ""));
    }

 */
}
