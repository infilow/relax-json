package com.infilos.relax.source;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.google.common.collect.Iterators;
import com.infilos.relax.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class JsonArrayDataSource extends AbstractDataSource {

    public JsonArrayDataSource(String json) {
        super();
        this.dataSource = JsonUtil.getInstance().createArrayNode(DataSource.trimBOM(json));
    }

    public JsonArrayDataSource(InputStream inStream) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inStream, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(inputStreamReader)) {
            String content = br.lines().collect(Collectors.joining("\n"));
            this.dataSource = JsonUtil.getInstance().createArrayNode(DataSource.trimBOM(content));
        }
    }

    @Override
    public boolean hasReliableHeaders() {
        return false;
    }

    @Override
    public Iterator<String[]> iterator() {
        String[] headers = headers();

        return Iterators.transform(((ArrayNode) dataSource).iterator(), (JsonNode input) -> {
            List<String> values = new ArrayList<>();
            for (String header : headers) {
                JsonNode val = input.get(header);
                values.add((null != val) ? val.asText("") : null);
            }

            return values.toArray(new String[0]);
        });
    }

    /**
     * This is a very costly operation that iterates through the whole data to find the headers.
     *
     * @return the union of keys for each entry in the JSON array
     */
    public String[] headers() {
        Set<String> headers = new LinkedHashSet<>();
        ((ArrayNode) dataSource)
            .elements()
            .forEachRemaining((firstObject) -> 
                firstObject.fields().forEachRemaining(f -> headers.add(f.getKey()))
            );

        return headers.toArray(new String[]{});
    }

    private CsvMapper getCsvMapper() {
        return CsvMapper.builder()
            .addModule(complexObjectSerializationModule())
            .build();
    }

    private Module complexObjectSerializationModule() {
        SimpleModule module = new SimpleModule();
        module.setSerializers(new SimpleSerializers());
        module.addSerializer(ObjectNode.class, mapSerializer());
        return module;
    }

    private JsonSerializer<ObjectNode> mapSerializer() {
        return new JsonSerializer<ObjectNode>() {
            @Override
            public void serialize(ObjectNode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString());
            }
        };
    }
}
