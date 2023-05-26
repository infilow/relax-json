package com.infilos.relax.hierarchy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.infilos.relax.Json;
import org.junit.Test;
import org.requirementsascode.moonwlker.MoonwlkerModule;

import java.math.BigDecimal;

public class SerdeTest {

    @Test
    public void test() {
//        MoonwlkerModule module = MoonwlkerModule.builder()
//            .fromProperty("type").toSubclassesOf(Animal.class)
//            .build();
//        Json.underMapper().registerModule(module);
//        Json.underMapper().registerModule(new ParameterNamesModule());
//        Json.underMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Cat cat = new Cat(BigDecimal.valueOf(22.1D), "Lila", "Toto", new Lives(1));
        String catEncoded = Json.from(cat).asPrettyString();
        System.out.println(catEncoded);
        Object catDecodedAsObject = Json.from(catEncoded).asObject(Object.class);
        Animal catDecodedAsAnimal = Json.from(catEncoded).asObject(Animal.class);
        Cat catDecodedAsCat = Json.from(catEncoded).asObject(Cat.class);
        System.out.println("Object: " + catDecodedAsObject);
        System.out.println("Object: " + catDecodedAsObject.getClass());
        System.out.println("Animal: " + catDecodedAsAnimal);
        System.out.println("Animal: " + catDecodedAsAnimal.getClass());
        System.out.println("Cat: " + catDecodedAsCat);
        System.out.println("Cat: " + catDecodedAsCat.getClass());

        Bird bird = new Bird(BigDecimal.valueOf(12D), "FAST", "Anna");
        String birdEncoded = Json.from(bird).asPrettyString();
        System.out.println(birdEncoded);
        Object birdDecodedAsObject = Json.from(birdEncoded).asObject(Object.class);
        Animal birdDecodedAsAnimal = Json.from(birdEncoded).asObject(Animal.class);
        Bird birdDecodedAsBird = Json.from(birdEncoded).asObject(Bird.class);
        System.out.println("Object: " + birdDecodedAsObject);
        System.out.println("Object: " + birdDecodedAsObject.getClass());
        System.out.println("Animal: " + birdDecodedAsAnimal);
        System.out.println("Animal: " + birdDecodedAsAnimal.getClass());
        System.out.println("Bird: " + birdDecodedAsBird);
        System.out.println("Bird: " + birdDecodedAsBird.getClass());

//        String jsonString = "{\"type\":\"Dog\",\"price\":412,\"name\":\"Calla\",\"command\":\"Sit\"}";
//        Dog dog = (Dog) objectMapper.readValue(jsonString, Animal.class);


    }
}
