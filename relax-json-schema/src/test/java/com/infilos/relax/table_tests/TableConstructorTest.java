package com.infilos.relax.table_tests;

import com.infilos.relax.Schema;
import com.infilos.relax.Table;
import com.infilos.relax.source.JsonArrayDataSource;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static com.infilos.relax.TestHelper.getTestDataDirectory;

class TableConstructorTest extends Assert {


    @Test
    //@DisplayName("Create a Table")
    void createTable1() throws Exception{
        Table table = new Table();
        assertNull(table.getSchema());
        assertNull(table.getDataSourceFormat());
    }

    @Test
    //@DisplayName("Create a Table And Set a Schema")
    void createTable2() throws Exception{
        Table table = new Table();
        File testDataDir = getTestDataDirectory();

        Schema schema = Schema.fromJson(new File(testDataDir, "schema/employee_schema.json"), true);
        table.setSchema(schema);
        assertNotNull(table.getSchema());
        assertNull(table.getDataSourceFormat());
    }


    @Test
    //@DisplayName("Create a Table And Set a DataSourceFormat")
    void createTable3() throws Exception{
        Table table = new Table();
        File testDataDir = getTestDataDirectory();

        try (InputStream inStream = new FileInputStream(new File(testDataDir, "data/population.json"))) {
            JsonArrayDataSource fmt = new JsonArrayDataSource(inStream);
            table.setDataSourceFormat(fmt);
        }

        assertNull(table.getSchema());
        assertNotNull(table.getDataSourceFormat());
    }

    @Test
    //@DisplayName("Create a Table And set a Schema and a DataSourceFormat")
    void createTable4() throws Exception{
        Table table = new Table();
        File testDataDir = getTestDataDirectory();

        Schema schema = Schema.fromJson(new File(testDataDir, "schema/population_schema.json"), true);
        try (InputStream inStream = new FileInputStream(new File(testDataDir, "data/population.json"))) {
            JsonArrayDataSource fmt = new JsonArrayDataSource(inStream);
            table.setDataSourceFormat(fmt);
        }

        table.setSchema(schema);
        assertNotNull(table.getSchema());
        assertNotNull(table.getDataSourceFormat());
    }

    @Test
    //@DisplayName("Create a Table And set a Schema and a DataSourceFormat - 2")
    void createTable5() throws Exception{
        Table table = new Table();
        File testDataDir = getTestDataDirectory();

        Schema schema = Schema.fromJson(new File(testDataDir, "schema/population_schema.json"), true);
        table.setSchema(schema);
        try (InputStream inStream = new FileInputStream(new File(testDataDir, "data/population.json"))) {
            JsonArrayDataSource fmt = new JsonArrayDataSource(inStream);
            table.setDataSourceFormat(fmt);
        }

        assertNotNull(table.getSchema());
        assertNotNull(table.getDataSourceFormat());
    }
}
