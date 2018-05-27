package com.zerox.base.helper;

import com.zerox.base.common.XLogger;

public class InitHelper {

    private static Class[] initClass = new Class[]{
            ClassHelper.class,
            IocHelper.class
    };


    public static void init(){
        for (Class aClass : initClass) {
            try {
                //注解的class放到容器中
                Class.forName(aClass.getName(), true, ClassLoader.getSystemClassLoader());
            } catch (Exception e) {
                XLogger.error("init helper class error [{}]", e);
            }
        }
    }
}
