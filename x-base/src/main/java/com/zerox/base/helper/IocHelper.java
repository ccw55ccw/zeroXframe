package com.zerox.base.helper;

import cn.hutool.core.util.StrUtil;
import com.zerox.base.annotation.Inject;
import com.zerox.base.bean.BeanContextContainer;
import com.zerox.base.bean.BeanObject;
import com.zerox.base.common.XLogger;

import java.lang.reflect.Field;
import java.util.Map;

public class IocHelper {

    static{
        scanBean();
    }

    private static final void scanBean() {
        Map<String, BeanObject> beanMap = BeanContextContainer.getContainer();
        for (Map.Entry<String, BeanObject> entry : beanMap.entrySet()) {
            BeanObject beanObject = entry.getValue();
            Object instance = beanObject.getBeanObj();
            Class<?> clazz = instance.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Inject inject = field.getAnnotation(Inject.class);
                    String beanName = inject.name();
                    if (StrUtil.isBlank(beanName)) {
                        beanName = field.getName();
                    }
                    BeanObject fieldBean = BeanContextContainer.getContainer().get(beanName);
                    if (fieldBean != null) {
                        try {
                            field.setAccessible(true);
                            field.set(instance, fieldBean.getBeanObj());
                        } catch (IllegalAccessException e) {
                            XLogger.error("ioc init bean error [{}]", e);
                        }
                    }
                }
            }

        }

    }
}
