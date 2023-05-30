package com.infilos.relax.source;

import com.infilos.relax.error.TableSchemaException;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

class StringArrayDataSourceFormatTest extends Assert {
    private static List<String[]> populationArr;

    private final static String populationCsv =
            "london,2017,8780000\n" +
            "paris,2017,2240000\n" +
            "rome,2017,2860000";

    private final static String populationjson = "[\n" +
            "  {\n" +
            "    \"city\": \"london\",\n" +
            "    \"year\": \"2017\",\n" +
            "    \"population\": 8780000\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"paris\",\n" +
            "    \"year\": \"2017\",\n" +
            "    \"population\": 2240000\n" +
            "  },\n" +
            "  {\n" +
            "    \"city\": \"rome\",\n" +
            "    \"year\": \"2017\",\n" +
            "    \"population\": 2860000\n" +
            "  }\n" +
            "]";

    private final static String[] populationHeaders = new String[]{
            "city", "year", "population"
    };


    @BeforeClass
    static void setUp() {
        String[] lines = populationCsv.split("\n");
        populationArr = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            String[] cols = lines[i].split(",");
            populationArr.add(cols);
        }
    }

    @Test
    ////@DisplayName("Test StringArrayDataSourceFormat creation and data reading")
    void testReadStringArrayDataSourceFormat() throws Exception{
        DataSource ds = new StringArrayDataSource(populationArr, populationHeaders);
        List<String[]> data = ds.data();
        assertEquals(populationArr, data);
    }

    /*
    @Test
    //@DisplayName("Test writing data as CSV")
    void writeCsvToFile() throws Exception{
        String content = null;
        File tempFile = Files.createTempFile("tableschema-", ".csv").toFile();

        DataSourceFormat ds = new StringArrayDataSourceFormat(populationArr, populationHeaders);
        ds.write(tempFile);

        try (FileReader fr = new FileReader(tempFile)) {
            try (BufferedReader rdr = new BufferedReader(fr)) {
                content = rdr.lines().collect(Collectors.joining("\n"));
            }
        }
        String testStr = String.join(",", populationHeaders) +"\n"+populationCsv;
        // evade the CRLF mess by nuking all CR chars
        Assertions.assertEquals(testStr.replaceAll("\\r", ""), content.replaceAll("\\r", ""));
    }
*/
    @Test
    ////@DisplayName("Test method is unimplemented")
    void testGetCSVParserIsUnimplemented() throws Exception{
        StringArrayDataSource ds = new StringArrayDataSource(populationArr, populationHeaders);
        assertThrows(TableSchemaException.class, ds::getCSVParser);
    }

}
