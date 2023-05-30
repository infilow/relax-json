package com.infilos.relax.source;

import com.infilos.relax.error.TableSchemaException;
import org.apache.commons.csv.CSVParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringArrayDataSource extends AbstractDataSource {
    private final String[] headers;

    public StringArrayDataSource(Collection<String[]> data, String[] headers) {
        this.dataSource = data;
        this.headers = headers;
    }

    CSVParser getCSVParser() {
        throw new TableSchemaException("Not implemented for StringArrayDataSourceFormat");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<String[]> iterator() throws Exception {
        return ((Collection<String[]>) dataSource).iterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String[]> data() throws Exception {
        return new ArrayList<>((Collection<String[]>) dataSource);
    }

    @Override
    public String[] headers() throws Exception {
        return headers;
    }

    @Override
    public boolean hasReliableHeaders() {
        return headers != null;
    }
}
