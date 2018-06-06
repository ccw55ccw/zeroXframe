package com.zerox.base.bean;

import cn.hutool.core.util.StrUtil;
import com.zerox.base.bean.validate.BeanXsdValidateResolver;
import com.zerox.base.common.XLogger;
import com.zerox.base.config.ApplicationConfig;
import com.zerox.base.context.ApplicationContext;
import com.zerox.base.function.ParseContextConfigurationFunction;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseContextConfiguration {

    public static Map<String, Bean> parseBeanXml(String path) {
        Map<String, Bean> result = new HashMap<String, Bean>();
        Document document = getDocument(path);
        List<Element> beans = document.getRootElement().elements("bean");
        for (Element beanEle : beans) {
            Bean bean = new Bean();
            bean.setClassName(beanEle.attributeValue("class"));
            bean.setName(beanEle.attributeValue("name"));
            String scope = beanEle.attributeValue("scope");
            if (StrUtil.isNotBlank(scope)) {
                bean.setScope(scope);
            }

            List<Element> beanProperties = beanEle.elements("property");
            for (Element beanProperty : beanProperties) {
                BeanProperty beanPro = new BeanProperty();
                beanPro.setName(beanProperty.attributeValue("name"));
                beanPro.setValue(beanProperty.attributeValue("value"));
                beanPro.setRef(beanProperty.attributeValue("ref"));
                bean.getProperties().add(beanPro);
            }

            result.put(bean.getName(), bean);

        }

        return result;

    }

    public static void parseConfigPropertyXml(String path){
        Document document = getDocument(path);
        Element element = document.getRootElement().element("properties");
        if (element != null) {
            List<Element> elements = element.elements("property");
            Map<String, Object> configMap = ApplicationConfig.getConfigMap();
            for (Element e : elements) {
                configMap.put(e.attributeValue("name"), e.attributeValue("value"));
            }
        }
    }

    private static Document getDocument(String path) {
        SAXReader reader = new SAXReader();
        reader.setValidation(true);
        reader.setEntityResolver(new BeanXsdValidateResolver());
        try {
            reader.setFeature("http://xml.org/sax/features/validation", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking",true);
        } catch (SAXException e) {
            XLogger.error("validating sax error, {}", e);
        }
        InputStream is = ParseContextConfiguration.class.getResourceAsStream(path);
        Document document = null;
        try {
            document = reader.read(is);
        } catch (DocumentException e) {
            XLogger.error("parse context xml error, {}", e);
        }
        return document;
    }
}
