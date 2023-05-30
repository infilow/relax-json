package com.infilos.relax.enumtest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.infilos.relax.Json;
import org.junit.Test;

import java.io.IOException;

public class SerdeTest {

    @Test
    public void test() {
        Json.underMapper().registerModule(buildModule());

        Person person = new Person();
        person.setName("Anna");
        person.setAge(22);
        person.setGender(Gender.FEMALE);
        person.setRegion(Region.OVERSEA);

        String encoded = Json.from(person).asPrettyString();
        System.out.println(encoded);

        Person decoded = Json.from(encoded).asObject(Person.class);
        System.out.println(decoded);
    }

    private com.fasterxml.jackson.databind.Module buildModule() {
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            @SuppressWarnings("unchecked")
            public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config,
                                                              JavaType type,
                                                              BeanDescription beanDescr,
                                                              JsonDeserializer<?> deserializer) {
                if (beanDescr.getBeanClass().isEnum()) {
                    if (CodeBasedEnum.class.isAssignableFrom(beanDescr.getBeanClass())) {
                        return new CodeBasedEnumDeserializer<>((Class<CodeBasedEnum<?>>) beanDescr.getBeanClass());
                    } else if (NameBasedEnum.class.isAssignableFrom(beanDescr.getBeanClass())) {
                        return new NameBasedEnumDeserializer<>((Class<NameBasedEnum<?>>) beanDescr.getBeanClass());
                    }
                }

                return deserializer;
            }
        });

        return module;
    }

    static class CodeBasedEnumDeserializer<T extends CodeBasedEnum<E>, E extends Enum<E>> extends JsonDeserializer<T> {

        private final Class<CodeBasedEnum<?>> enumClass;

        public CodeBasedEnumDeserializer(Class<CodeBasedEnum<?>> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            TreeNode node = jsonParser.getCodec().readTree(jsonParser);
            Integer code = null;
            try {
                if (node.isValueNode()) {
                    ValueNode valueNode = (ValueNode) node;
                    code = Integer.parseInt(valueNode.asText());

                    return CodeBasedEnum.fromCode((Class<T>) enumClass, code);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException(String.format("%s cannot match the code: %s", enumClass.getSimpleName(), code));
            }

            return null;
        }
    }

    static class NameBasedEnumDeserializer<T extends NameBasedEnum<E>, E extends Enum<E>> extends JsonDeserializer<T> {

        private final Class<NameBasedEnum<?>> enumClass;

        public NameBasedEnumDeserializer(Class<NameBasedEnum<?>> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            TreeNode node = jsonParser.getCodec().readTree(jsonParser);
            String name = null;
            try {
                if (node.isValueNode()) {
                    ValueNode valueNode = (ValueNode) node;
                    name = valueNode.asText();

                    return NameBasedEnum.fromName((Class<T>) enumClass, name);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException(String.format("%s cannot match the name: %s", enumClass.getSimpleName(), name));
            }

            return null;
        }
    }
}
