package com.infilos.relax.enumtest;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender implements CodeBasedEnum<Gender> {
    MALE(1),
    FEMALE(0);

    private final int code;

    Gender(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

//    @JsonCreator
//    public static Gender fromCode(int code) {
//        return CodeBasedEnum.fromCode(Gender.class, code);
//    }
}
