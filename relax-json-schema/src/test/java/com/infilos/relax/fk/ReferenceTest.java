package com.infilos.relax.fk;

import java.util.ArrayList;
import java.util.List;

import com.infilos.relax.schema.Reference;
import com.infilos.relax.error.ForeignKeyException;
import com.infilos.relax.util.JsonUtil;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 */
public class ReferenceTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testValidStringFieldsReference() throws ForeignKeyException {
        Reference ref = new Reference("resource", "field");
        
        // Validation set to strict=true and no exception has been thrown.
        // Test passes.
        Assert.assertNotNull(ref);
    }

    @Test
    public void testValidArrayFieldsReference() throws ForeignKeyException{
        // TODO: change this test after we remove org.json from Reference validator
        List<String> fields = new ArrayList<>();
        fields.add("field1");
        fields.add("field2");

        Reference ref = new Reference("resource", JsonUtil.getInstance().createArrayNode(fields));

        // Validation set to strict=true and no exception has been thrown.
        // Test passes.
        Assert.assertNotNull(ref);
    }

    @Test
    public void testNullFields() throws ForeignKeyException{
        exception.expectMessage("A foreign key's reference must have the fields and resource properties.");
        Reference ref = new Reference(null, "resource", true);
    }

    @Test
    public void testNullResource() throws ForeignKeyException{
        Reference ref = new Reference();
        ref.setFields("aField");

        exception.expectMessage("A foreign key's reference must have the fields and resource properties.");
        ref.validate();
    }

    @Test
    public void testNullFieldsAndResource() throws ForeignKeyException{
        Reference ref = new Reference();
        exception.expectMessage("A foreign key's reference must have the fields and resource properties.");
        ref.validate();
    }

    @Test
    public void testInvalidFieldsType() throws ForeignKeyException{
        exception.expectMessage("The foreign key's reference fields property must be a string or an array.");
        Reference ref = new Reference("resource", 123, true);
    }
}
