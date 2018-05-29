package com.zerox.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestMapping {
    String value();
    enum Method{
        GET,
        POST
    }
    Method method() default Method.GET;
}
