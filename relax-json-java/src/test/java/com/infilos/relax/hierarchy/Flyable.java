package com.infilos.relax.hierarchy;

import java.math.BigDecimal;

public class Flyable extends Animal {
    private String mode;

    public Flyable() {
        super();
    }

    public Flyable(BigDecimal price, String mode) {
        super(price);
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
