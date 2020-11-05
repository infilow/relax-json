package com.infilos.relax.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * @author zhiguang.zhang on 2020-11-05.
 * 
 * Provide a way to register ser/des with instance.
 */

public interface JsonSerdes<T> {
    
    Class<T> onClass();
    
    JsonSerializer<T> serializer();
    
    JsonDeserializer<T> deserializer();
}
