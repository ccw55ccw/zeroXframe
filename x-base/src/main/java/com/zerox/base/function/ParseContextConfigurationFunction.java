package com.zerox.base.function;

import org.dom4j.Element;

import java.util.Map;
import java.util.function.Consumer;

public class ParseContextConfigurationFunction {

    public static Consumer<Element> loopConfigPropertiesConsumer(Map<String, Object> configMap) {
        return e -> configMap.put(e.attributeValue("name"), e.attributeValue("value"));
    }

}

