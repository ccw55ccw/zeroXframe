package com.zerox.base.web;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class Handler {
    private Class<?> controllerClass;
    private Method handleMethod;
}
