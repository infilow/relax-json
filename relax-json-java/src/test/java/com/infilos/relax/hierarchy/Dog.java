package com.infilos.relax.hierarchy;

import java.math.BigDecimal;

public class Dog extends Animal {
    private final String name;
    private final String command;

    public Dog(BigDecimal price, String name, String command) {
        super(price);
        this.name = name;
        this.command = command;
    }

    public String name() {
        return name;
    }

    public String command() {
        return command;
    }
}