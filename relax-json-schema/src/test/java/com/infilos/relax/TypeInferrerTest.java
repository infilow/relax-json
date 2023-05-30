package com.infilos.relax;

import com.infilos.utils.Resource;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TypeInferrerTest extends Assert {

    @Test
    public void test() throws URISyntaxException, IOException {
        String string = String.join("\n", Resource.readAsLines("/fixtures/dates_data.csv"));
        Table table = Table.fromSource(string);
        System.out.println(table.inferSchema(100).toJsonString());
    }

    @Test
    public void testCSV() throws URISyntaxException, FileNotFoundException {
        File srouce = Resource.readAsFile("/fixtures/dates_data.csv");
        CsvReader csvReader = CsvReader.builder().build(new FileReader(srouce));
        int count = 0;
        for (CsvRow row : csvReader) {
            if (count < 10) {
                System.out.println(row.getOriginalLineNumber() + ": " + row.getFields());
            } else {
                break;
            }
            count++;
        }
    }

    public static List<String> readAsLines(String pathOfResource, int limit) throws IOException {
        InputStream stream = readAsStream(pathOfResource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = new ArrayList<>();

        while (reader.ready()) {
            lines.add(reader.readLine());

            if (limit >= 0 && lines.size() >= limit) {
                break;
            }
        }

        return lines;
    }

    public static InputStream readAsStream(String pathOfResource) {
        return Resource.class.getResourceAsStream(fixPathOfResource(pathOfResource));
    }

    private static String fixPathOfResource(String pathOfResource) {
        return pathOfResource.startsWith("/") ? pathOfResource : "/" + pathOfResource;
    }
}
