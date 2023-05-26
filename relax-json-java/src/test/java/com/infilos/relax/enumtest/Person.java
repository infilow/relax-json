package com.infilos.relax.enumtest;

public class Person {
    private String name;
    private Integer age;
    private Gender gender;
    private Region region;

    public Person() {
    }

    public Person(String name, Integer age, Gender gender, Region region) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Person{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", gender=" + gender +
            ", region=" + region +
            '}';
    }
}
