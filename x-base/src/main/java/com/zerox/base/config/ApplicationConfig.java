package com.zerox.base.config;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig {

    private static final Map<String, Object> configMap = new HashMap<>();

    public static Map<String, Object> getConfigMap(){
        return configMap;
    }

    public static String getConfig(ConfigPropertyKeyConstants constants) {
        return (String) configMap.get(constants.name());
    }
}
