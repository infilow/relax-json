package com.infilos.relax.iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.google.common.util.concurrent.AtomicDouble;
import com.infilos.relax.Table;
import com.infilos.relax.api.FieldFormat;
import com.infilos.relax.error.TableSchemaException;
import com.infilos.relax.field.Field;
import com.infilos.relax.field.ObjectField;
import com.infilos.relax.schema.BeanSchema;
import com.infilos.relax.util.JsonUtil;
import com.infilos.relax.Schema;
import org.geotools.geometry.iso.io.wkt.Coordinate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * Based on a Java Bean class instead of a {@link Schema}.
 * It therefore disregards the Schema set on the {@link Table} the iterator works on but creates its own Schema from the supplied `beanType`.
 * </pre>
 *
 * @param <T> the Bean class this BeanIterator expects
 */
public class BeanIterator<T> extends TableIterator<T> {
    private final Class<T> type;
    private final CsvMapper mapper = new CsvMapper();

    public BeanIterator(Table table, Class<T> beanType, boolean relations) throws Exception {
        this.type = beanType;
        this.relations = relations;
        init(table);
    }

    /**
     * Overrides {@link TableIterator#init(Table)} and instead of copying the Schema from the Table, infers a Schema from the Bean type.
     *
     * @param table The Table to iterate data on
     * @throws Exception in case header parsing, Schema inferral or Schema validation fails
     */
    @Override
    void init(Table table) throws Exception {
        mapping = table.getSchemaHeaderMapping();
        headers = table.getHeaders();
        schema = BeanSchema.infer(type);
        table.validate();
        wrappedIterator = table.getDataSourceFormat().iterator();
    }

    @Override
    public T next() {
        T retVal;
        final String[] row = super.wrappedIterator.next();

        try {
            retVal = type.newInstance();
            for (int i = 0; i < row.length; i++) {
                String fieldName = headers[i];
                Field field = schema.getField(fieldName);
                if (null == field) {
                    continue;
                }
                
                AnnotatedField annotatedField = ((BeanSchema) schema).getAnnotatedField(fieldName);
                FieldFormat annotation = annotatedField.getAnnotation(FieldFormat.class);
                String fieldFormat = field.getFormat();
        
                if (null != annotation) {
                    fieldFormat = annotation.value();
                } else {
                    // we may have a field that can have different formats
                    // but the Schema doesn't know about the true format
                    if (fieldFormat.equals(Field.FIELD_FORMAT_DEFAULT)) {
                        // have to parse format here when we have actual sample data
                        // instead of at BeanSchema inferral time
                        fieldFormat = field.parseFormat(row[i], null);
                    }
                }
                field.setFormat(fieldFormat);
                
                Object val = field.castValue(row[i]);
                if (null == val) {
                    continue;
                }
                    
                Class<?> annotatedFieldClass = annotatedField.getRawType();
                annotatedField.fixAccess(true);
                if (Number.class.isAssignableFrom(annotatedFieldClass)) {
                    setNumberField(retVal, annotatedField, (Number) val);
                } else if (byte.class.equals(annotatedFieldClass)) {
                    annotatedField.setValue(retVal, new Byte(((BigInteger) val).shortValue() + ""));
                } else if (short.class.equals(annotatedFieldClass)) {
                    annotatedField.setValue(retVal, ((BigInteger) val).shortValue());
                } else if (int.class.equals(annotatedFieldClass)) {
                    annotatedField.setValue(retVal, ((BigInteger) val).intValue());
                } else if (long.class.equals(annotatedFieldClass)) {
                    annotatedField.setValue(retVal, ((BigInteger) val).longValue());
                } else if (float.class.equals(annotatedFieldClass)) {
                    annotatedField.setValue(retVal, ((BigDecimal) val).floatValue());
                } else if (double.class.equals(annotatedFieldClass)) {
                    annotatedField.setValue(retVal, ((BigDecimal) val).doubleValue());
                } else if (UUID.class.equals(annotatedFieldClass)) {
                    annotatedField.setValue(retVal, UUID.fromString((String) val));
                } else if (Coordinate.class.isAssignableFrom(annotatedFieldClass)) {
                    double[] arr = (double[]) val;
                    Coordinate coordinate = new Coordinate(arr[0], arr[1]);
                    annotatedField.setValue(retVal, coordinate);
                } else if (field instanceof ObjectField) {
                    if (annotatedFieldClass.equals(JsonNode.class)) {
                        annotatedField.setValue(retVal, JsonUtil.getInstance().readValue(val.toString()));
                    } else {
                        // this conversion method may also be used for the other field types
                        annotatedField.setValue(retVal, JsonUtil.getInstance().convertValue(val, annotatedFieldClass));
                    }
                } else {
                    annotatedField.setValue(retVal, val);
                }
            }
            return retVal;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new TableSchemaException(e);
        }
    }

    private void setNumberField(T obj, AnnotatedField field, Number val) {
        Class<?> fClass = field.getRawType();
        if (fClass.equals(BigDecimal.class)) {
            BigDecimal big = new BigDecimal(val.toString());
            field.setValue(obj, big);
        } else if (fClass.equals(Float.class)) {
            field.setValue(obj, (val.floatValue()));
        } else if (fClass.equals(Double.class)) {
            field.setValue(obj, (val.doubleValue()));
        } else if (fClass.equals(Integer.class)) {
            field.setValue(obj, (val.intValue()));
        } else if (fClass.equals(Long.class)) {
            field.setValue(obj, (val.longValue()));
        } else if (fClass.equals(Short.class)) {
            field.setValue(obj, (val.shortValue()));
        } else if (fClass.equals(Byte.class)) {
            field.setValue(obj, (val.byteValue()));
        } else if (fClass.equals(BigInteger.class)) {
            BigInteger big = new BigInteger(val.toString());
            field.setValue(obj, big);
        } else if (fClass.equals(AtomicInteger.class)) {
            AtomicInteger ai = new AtomicInteger();
            ai.set(val.intValue());
            field.setValue(obj, ai);
        } else if (fClass.equals(AtomicLong.class)) {
            AtomicLong ai = new AtomicLong();
            ai.set(val.longValue());
            field.setValue(obj, ai);
        } else if (fClass.equals(AtomicDouble.class)) {
            AtomicDouble ai = new AtomicDouble();
            ai.set(val.doubleValue());
            field.setValue(obj, ai);
        }
    }
}
