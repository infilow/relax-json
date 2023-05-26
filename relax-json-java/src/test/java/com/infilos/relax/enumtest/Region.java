package com.infilos.relax.enumtest;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Region implements NameBasedEnum<Region> {
    CHINA,
    OVERSEA;

//    @JsonCreator
//    public static Region fromName(String name) {
//        return NameBasedEnum.fromName(Region.class, name);
//    }
}
