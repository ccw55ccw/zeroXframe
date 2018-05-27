package com.zerox.base.context;

import cn.hutool.core.util.StrUtil;
import com.zerox.base.bean.*;
import com.zerox.base.common.XLogger;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApplicationContext implements BeanFactory {

    Map<String, Bean> configContextMap;


    Map<String, BeanObject> beanContextMap = BeanContextContainer.getContainer();

    BeanObject getObjectFromBean(Bean bean) {
        Map<String, Object> beanParam = new HashMap<>();
        BeanObject beanObject = null;
        try {
            Class clazz = Class.forName(bean.getClassName());
            Object instance = clazz.newInstance();
            for (BeanProperty beanProperty : bean.getProperties()) {
                if (beanProperty.getValue() != null) {
                    beanParam.put(beanProperty.getName(), beanProperty.getValue());
                    BeanUtils.populate(instance, beanParam);
                } else if (beanProperty.getRef() != null) {
                    BeanObject refBeanObject = beanContextMap.get(beanProperty.getRef());
                    if (refBeanObject == null) {
                        refBeanObject = getObjectFromBean(configContextMap.get(beanProperty.getRef()));
                        beanParam.put(beanProperty.getName(), refBeanObject.getBeanObj());
                    }
                    beanParam.put(beanProperty.getName(), refBeanObject.getBeanObj());
                    BeanUtils.populate(beanProperty.getName(), beanParam);
                }
            }
            beanObject = new BeanObject();
            beanObject.setBeanObj(instance);
            beanObject.setClassName(bean.getClassName());
            beanObject.setName(bean.getName());
            if (isSingleton(bean)) {
                beanContextMap.put(bean.getName(), beanObject);
            }

        } catch (ClassNotFoundException e) {
            XLogger.error("class not found, {}", bean.getClassName());
        } catch (IllegalAccessException e) {
            XLogger.error("has no access to create, {}", bean.getClassName());
        } catch (InstantiationException e) {
            XLogger.error("instant error, {}", bean.getClassName());
        } catch (InvocationTargetException e) {
            XLogger.error("set bean property error, {}, {}", bean.getClassName(), beanParam.toString());
        }
        return beanObject;

    }

    @Override
    public Object getBean(String name) {
        BeanObject beanObject = beanContextMap.get(name);
        if (beanObject == null) {
            beanObject = getObjectFromBean(configContextMap.get(name));
        }
        return beanObject.getBeanObj();
    }

    boolean isSingleton(Bean bean) {
        return StrUtil.isBlank(bean.getScope()) || BeanScope.SINGILTON.name()
                .equalsIgnoreCase(bean.getScope());
    }

}
