package com.zerox.base.bean;

import java.util.HashMap;
import java.util.Map;

public class BeanContextContainer {

    private static final Map<String, BeanObject> CONTAINER = new HashMap<>();

    public static Map<String, BeanObject> getContainer(){
        return CONTAINER;
    }
}
