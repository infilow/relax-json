package com.infilos.relax.source;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataSource implements DataSource {
    protected String[] headers;
    protected Object dataSource = null;
    protected File workDir;

    AbstractDataSource(){}

    AbstractDataSource(URL dataSource){
        this.dataSource = dataSource;
    }

    AbstractDataSource(File dataSource, File workDir){
        this.dataSource = dataSource;
        this.workDir = workDir;
    }

    AbstractDataSource(String dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public List<String[]> data() throws Exception{
        List<String[]> data = new ArrayList<>();
        iterator().forEachRemaining(data::add);
        return data;
    }

    String readFileContents(String path) throws IOException {
        return DataSource.readFileContents(path, workDir);
    }
}
