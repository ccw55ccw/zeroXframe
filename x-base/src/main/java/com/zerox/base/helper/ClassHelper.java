package com.zerox.base.helper;

import cn.hutool.core.util.StrUtil;
import com.zerox.base.annotation.Action;
import com.zerox.base.annotation.Service;
import com.zerox.base.annotation.XBean;
import com.zerox.base.bean.BeanContextContainer;
import com.zerox.base.bean.BeanObject;
import com.zerox.base.common.XLogger;
import com.zerox.base.config.ApplicationConfig;
import com.zerox.base.config.ConfigPropertyKeyConstants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassHelper {

    public static Class[] annotationClazzs = new Class[]{
            Service.class,
            XBean.class,
            Action.class
    };

    public static final List<Class> classes = new ArrayList<>();

    private static ClassLoader classloader = ClassHelper.class.getClassLoader();

    static {
        String basePackage = ApplicationConfig.getConfig(ConfigPropertyKeyConstants.basePackage);
        addClass(basePackage);
        classToBean();
    }

    private static void addClass(String basePackage){
        URL url = classloader.getResource(basePackage.replace(".", "/"));
        String protocol = url.getProtocol();
        if (StrUtil.equals("file", protocol)) {
            findLocalClass(basePackage);
        } else if (StrUtil.equals("jar", protocol)) {
            findJarClass(basePackage);
        }
    }

    private static void findLocalClass(String basePackage) {

        try {
            URI url = classloader.getResource(basePackage.replace(".", "/")).toURI();
            Path path = Paths.get(url);
            Stack<String> classPathStack = new Stack<>();
            Files.walkFileTree(path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    String name = dir.toFile().getName();
                    XLogger.debug("scan class dir [{}]", name);
                    classPathStack.push(name);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    File classFile = file.toFile();
                    XLogger.debug("add class [{}]", classFile.getName());
                    if (classFile.getName().endsWith(".class")) {
                        StringBuilder dirName = new StringBuilder();
                        boolean isBase = true;
                        for (String eleStr:classPathStack) {
                            if (isBase) {
                                isBase = false;
                                continue;
                            }
                            dirName.append(".")
                                    .append(eleStr);
                        }
                        String classPath = basePackage + dirName + "." + classFile.getName().replace(".class", "");
                        try {
                            Class<?> clazz = classloader.loadClass(classPath);
                            classes.add(clazz);
                        } catch (ClassNotFoundException e) {
                            XLogger.error("class not found error [{}]", e);
                        }
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    XLogger.error("visit file fail [{}]", exc);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    classPathStack.pop();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException | URISyntaxException e) {
            XLogger.error("find local class error [{}]", e);
        }

    }

    private static void findJarClass(String basePackaName) {
        String pathName = basePackaName.replace(".", "/");
        JarFile jarFile = null;
        URL url = classloader.getResource(pathName);
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (IOException e) {
            XLogger.error("jar url connection error [{}]", e);
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
                if (jarEntry.isDirectory()) {
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = null;
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    findJarClass(prefix);
                }
            }

            if (jarEntry.getName().endsWith(".class")) {
                Class<?> clazz;
                try {
                    clazz = classloader.loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    XLogger.error("class not found error [{}]", e);
                }

            }
        }
    }

    private static void classToBean(){
        for (Class aClass : ClassHelper.classes) {
            if (isAnnotationPresent(aClass)) {
                String beanName = getAnnotationName(aClass);
                Object instance = null;
                try {
                    instance = aClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    XLogger.error("new instance error ");
                }
                BeanObject beanObject = new BeanObject();
                if (StrUtil.isNotBlank(beanName)) {
                    beanObject.setName(beanName);
                } else {
                    beanObject.setName(aClass.getName());
                }
                beanObject.setBeanObj(instance);
                beanObject.setClassName(aClass.getName());
                Map<String, BeanObject> beanContextMap = BeanContextContainer.getContainer();
                beanContextMap.put(beanObject.getName(), beanObject);
            }
        }
        System.out.println(BeanContextContainer.getContainer());
    }

    private static boolean isAnnotationPresent(Class clazz) {

        for (Class c : ClassHelper.annotationClazzs) {
            if (clazz.isAnnotationPresent(c)) {
                return true;
            }
        }

        return false;
    }

    private static String getAnnotationName(Class clazz) {
        String res;
        for (Class c : ClassHelper.annotationClazzs) {
            if (clazz.isAnnotationPresent(c)) {
                Object annotationObj = clazz.getAnnotation(c);
                try {
                    for (Method method : c.getMethods()) {
                        if ("name".equals(method.getName())) {
                            Method nameMethod = c.getMethod("name");
                            res = (String) nameMethod.invoke(annotationObj);
                            return res;
                        }
                    }
                    return clazz.getSimpleName();
                } catch (NoSuchMethodException e) {
                    XLogger.error("{} invoke method error {}", c, e);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    XLogger.error("invoke method illegalaccess or invoke target error {}", e);
                }
            }
        }
        return null;
    }


}
