package com.infilos.relax.iterator;

import com.google.common.util.concurrent.AtomicDouble;
import com.infilos.relax.*;
import com.infilos.relax.beans.*;
import com.infilos.relax.field.*;
import com.infilos.relax.schema.BeanSchema;
import com.infilos.relax.Schema;
import com.infilos.relax.source.DataSource;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.infilos.relax.TestHelper.getTestDataDirectory;

class TableBeanIteratorTest extends Assert {
    private static Schema employeeSchema = null;
    private static Schema employeeSchemaAlternateDateForma = null;
    private static Schema gdpSchema = null;
    private static Table employeeTable = null;
    private static Table employeeTableAlternateDateFormat = null;
    private static Table gdpTable = null;

    @Before
    void setUp() throws Exception {
        File f = new File(getTestDataDirectory(), "schema/population_schema.json");
        Schema validPopulationSchema = null;
        try (FileInputStream fis = new FileInputStream(f)) {
            validPopulationSchema = Schema.fromJson (fis, false);
        }
        File testDataDir = getTestDataDirectory();

        File file = new File("data/employee_data.csv");
        employeeSchema = BeanSchema.infer(EmployeeBean.class);
        employeeTable = Table.fromSource(file, testDataDir, employeeSchema, DataSource.getDefaultCsvFormat());

        file = new File("data/employee_alternate_date_format.csv");
        employeeSchemaAlternateDateForma = BeanSchema.infer(EmployeeBeanWithAnnotation.class);
        employeeTableAlternateDateFormat = Table.fromSource(
                file,
                testDataDir,
                employeeSchemaAlternateDateForma,
                DataSource.getDefaultCsvFormat());

        file = new File("data/gdp.csv");
        gdpSchema = BeanSchema.infer(GrossDomesticProductBean.class);
        gdpTable = Table.fromSource(file, testDataDir, gdpSchema, DataSource.getDefaultCsvFormat());

    }


    @Test
    //@DisplayName("Test deserialization of EmployeeBean")
    void testBeanDeserialization() throws Exception {
        List<EmployeeBean> employees = new ArrayList<>();
        BeanIterator<EmployeeBean> bit = new BeanIterator<>(employeeTable, EmployeeBean.class, false);
        while (bit.hasNext()) {
            EmployeeBean employee = bit.next();
            employees.add(employee);
        }
        assertEquals(3, employees.size());
        EmployeeBean frank = employees.get(1);
        assertEquals("Frank McKrank", frank.getName());
        assertEquals("1992-02-14", new DateField("date").formatValueAsString(frank.getDateOfBirth(), null, null));
        assertFalse(frank.getAdmin());
        assertEquals("(90.0, 45.223, NaN)", frank.getAddressCoordinates().toString());
        assertEquals("PT15M", frank.getContractLength().toString());
        Map info = frank.getInfo();
        assertEquals(45, info.get("pin"));
        assertEquals(83.23, info.get("rate"));
        assertEquals(90, info.get("ssn"));
    }

    @Test
    //@DisplayName("Test deserialization of EmployeeBean with Annotation")
    void testBeanDeserialization2() throws Exception {
        List<EmployeeBeanWithAnnotation> employees = new ArrayList<>();
        BeanIterator<EmployeeBeanWithAnnotation> bit = new BeanIterator<>(employeeTableAlternateDateFormat, EmployeeBeanWithAnnotation.class, false);
        while (bit.hasNext()) {
            EmployeeBeanWithAnnotation employee = bit.next();
            employees.add(employee);
        }
        assertEquals(3, employees.size());
        EmployeeBeanWithAnnotation frank = employees.get(1);
        assertEquals("Frank McKrank", frank.getName());
        assertEquals("1992-02-14", new DateField("date").formatValueAsString(frank.getDateOfBirth(), null, null));
        assertFalse(frank.getAdmin());
        assertEquals("(90.0, 45.223, NaN)", frank.getAddressCoordinates().toString());
        assertEquals("PT15M", frank.getContractLength().toString());
        Map info = frank.getInfo();
        assertEquals(45, info.get("pin"));
        assertEquals(83.23, info.get("rate"));
        assertEquals(90, info.get("ssn"));
    }

    @Test
    //@DisplayName("Test deserialization of big floats (GrossDomesticProductBean)")
    void testBeanDeserialization3() throws Exception {
        List<GrossDomesticProductBean> records = new ArrayList<>();
        BeanIterator<GrossDomesticProductBean> bit
                = new BeanIterator<>(gdpTable, GrossDomesticProductBean.class, false);

        while (bit.hasNext()) {
            GrossDomesticProductBean record = bit.next();
            records.add(record);
        }
        assertEquals(11507, records.size());
    }

    @Test
    //@DisplayName("Test deserialization of various numbers")
    void testBeanDeserialization4() throws Exception {
        NumbersBean bn = new NumbersBean();
        bn.setAtomicIntegerVal(new AtomicInteger(2345123));
        bn.setBigDecimalVal(new BigDecimal("3542352304245234542345345423453.02345234"));
        bn.setBigIntVal(new BigInteger("23459734123456676123981234"));
        bn.setByteVal(new Byte("126"));
        bn.setId(23143245);
        bn.setLongVal(893479850249L);
        bn.setLongClassVal(908347392304952L);
        bn.setIntVal(234534);
        bn.setShortVal((short)234);
        bn.setFloatVal(3245.1234f);
        bn.setDoubleVal(345234552345.2345);
        bn.setDoubleClassVal(3.4567347437347346E23);
        bn.setFloatClassVal(2.34566246E9f);
        bn.setAtomicLongVal(new AtomicLong(234597341234502345L));
        bn.setAtomicDoubleVal(new AtomicDouble(3453254.34));

        Schema schema = null;
        File f = new File(getTestDataDirectory(), "schema/number_types_schema.json");
        try (FileInputStream fis = new FileInputStream(f)) {
            schema = Schema.fromJson (fis, false);
        }
        File dataFile = new File("data/number_types.csv");
        Table numbersTable
                = Table.fromSource(dataFile, getTestDataDirectory(), schema, DataSource.getDefaultCsvFormat());
        BeanIterator<NumbersBean> bit = numbersTable.iterator(NumbersBean.class, false);

        NumbersBean record = bit.next();
        assertEquals(bn, record);
    }

}