package com.infilos.relax.hierarchy;

import java.math.BigDecimal;

public class Cat extends Animal {
    private String name;
    private String nickname;
    private Lives lives;

    public Cat() {
        super();
    }

    public Cat(BigDecimal price, String name, String nickname, Lives lives) {
        super(price);
        this.name = name;
        this.nickname = nickname;
        this.lives = lives;
    }

    @Override
    public String toString() {
        return "Cat{" +
            "name='" + name + '\'' +
            ", nickname='" + nickname + '\'' +
            ", lives=" + lives +
            '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Lives getLives() {
        return lives;
    }

    public void setLives(Lives lives) {
        this.lives = lives;
    }
}
