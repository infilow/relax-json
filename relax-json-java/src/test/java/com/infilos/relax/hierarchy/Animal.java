package com.infilos.relax.hierarchy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=Cat.class, name="Cat"),
    @JsonSubTypes.Type(value=Dog.class, name="Dog"),
    @JsonSubTypes.Type(value=Bird.class, name="Bird")
})
public abstract class Animal {
    private BigDecimal price;

    public Animal() {
    }

    public Animal(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}