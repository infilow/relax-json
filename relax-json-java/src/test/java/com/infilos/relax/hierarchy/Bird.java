package com.infilos.relax.hierarchy;

import java.math.BigDecimal;

public class Bird extends Flyable {
    private String name;

    public Bird() {
        super();
    }

    public Bird(BigDecimal price, String mode, String name) {
        super(price, mode);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Bird{" +
            "name='" + name + '\'' +
            '}';
    }
}
