package com.infilos.relax.hierarchy;

public class Lives {
    private Integer lives;

    public Lives() {
    }

    public Lives(Integer lives) {
        this.lives = lives;
    }

    public Integer getLives() {
        return lives;
    }

    public void setLives(Integer lives) {
        this.lives = lives;
    }

    @Override
    public String toString() {
        return "Lives{" +
            "lives=" + lives +
            '}';
    }
}
