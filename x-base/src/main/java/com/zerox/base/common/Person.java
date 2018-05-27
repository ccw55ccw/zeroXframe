package com.zerox.base.common;

import lombok.Data;

@Data
public class Person {

    private String name;

    private Integer age;

    @Override
    public String toString(){
        return name + "-->" + age;
    }
}
