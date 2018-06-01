package com.zerox.base.context;

import cn.hutool.core.util.StrUtil;
import com.zerox.base.annotation.Service;
import com.zerox.base.bean.Bean;
import com.zerox.base.bean.BeanContextContainer;
import com.zerox.base.bean.BeanObject;
import com.zerox.base.bean.ParseContextConfiguration;
import com.zerox.base.common.XLogger;
import com.zerox.base.helper.ClassHelper;
import com.zerox.base.helper.InitHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext extends AbstractApplicationContext {

    private static ApplicationContext applicationContext;

    private String defaultContextPath = "/applicationContext.xml";

    private String contextPath;

    public ApplicationContext(String path) {
        if (StrUtil.isBlank(path)) {
            path = defaultContextPath;
        }
        this.contextPath = path;

        init();

        InitHelper.init();

        applicationContext = this;
    }

    private void init() {

        ParseContextConfiguration.parseConfigPropertyXml(contextPath);

        beanContextMap = BeanContextContainer.getContainer();
        configContextMap = ParseContextConfiguration.parseBeanXml(contextPath);

        for (Map.Entry<String, Bean> beanEntry : configContextMap.entrySet()) {
            boolean isSingleton = isSingleton(beanEntry.getValue());
            if (isSingleton) {
                BeanObject beanObject = getObjectFromBean(beanEntry.getValue());
                beanContextMap.put(beanEntry.getKey(), beanObject);
            }
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

}
