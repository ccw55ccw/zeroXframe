package com.zerox.base.context;

import com.zerox.base.bean.Bean;
import com.zerox.base.bean.BeanObject;
import com.zerox.base.bean.ParseContextConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ClassXmlApplicationContext extends AbstractApplicationContext {

    public ClassXmlApplicationContext(String path) {
        beanContextMap = new HashMap<>();
        configContextMap = ParseContextConfiguration.parseBeanXml(path);

        for (Map.Entry<String, Bean> beanEntry : configContextMap.entrySet()) {
            boolean isSingleton = isSingleton(beanEntry.getValue());
            if (isSingleton) {
                BeanObject beanObject = getObjectFromBean(beanEntry.getValue());
                beanContextMap.put(beanEntry.getKey(), beanObject);
            }
        }
    }

}
