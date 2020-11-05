package com.infilos.relax.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.util.ServiceLoader;

/**
 * @author infilos on 2020-06-13.
 */

public class JsonMappers {
    protected JsonMappers() {
    }

    public static final ObjectMapper JavaMapper = new ObjectMapper();
    public static final ObjectMapper ScalaMapper = new ObjectMapper();
    private static final SimpleModule SerdesModule = buildSerdesModule();

    static {
        ScalaMapper.registerModule(new Jdk8Module());
        ScalaMapper.registerModule(new JavaTimeModule());
        ScalaMapper.registerModule(new ParameterNamesModule());
        ScalaMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        ScalaMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ScalaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        ScalaMapper.registerModule(SerdesModule);

        JavaMapper.registerModule(new Jdk8Module());
        JavaMapper.registerModule(new JavaTimeModule());
        JavaMapper.registerModule(new ParameterNamesModule());
        JavaMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JavaMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JavaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        JavaMapper.registerModule(SerdesModule);
    }

    public static void register(Module module) {
        JavaMapper.registerModule(module);
        ScalaMapper.registerModule(module);
    }

    public static void register(Module... modules) {
        JavaMapper.registerModules(modules);
        ScalaMapper.registerModules(modules);
    }
    
    @SuppressWarnings("unchecked")
    private static SimpleModule buildSerdesModule() {
        SimpleModule module = new SimpleModule();
        
        ServiceLoader.load(JsonSerdes.class).forEach(serdes -> {
            if(serdes.onClass() == null) {
               return; 
            }
            if(serdes.serializer() != null) {
                module.addSerializer(serdes.onClass(), serdes.serializer());
            }
            if(serdes.deserializer() != null) {
                module.addDeserializer(serdes.onClass(), serdes.deserializer());
            }
        });
        
        return module;
    }
}
