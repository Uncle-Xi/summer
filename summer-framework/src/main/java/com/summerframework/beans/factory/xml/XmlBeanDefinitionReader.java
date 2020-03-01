package com.summerframework.beans.factory.xml;

import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.beans.factory.support.BeanDefinitionRegistry;
import com.summerframework.core.io.ResourceLoader;

/**
 * @description: XmlBeanDefinitionReader
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XmlBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void setResourceLoader( ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void loadBeanDefinitions(String location) {
        BeanDefinitionReaderUtils.registerClassAnnotationDefinition(registry);
        doLoadBeanDefinitions(location);
    }

    protected void doLoadBeanDefinitions(String location) {
        BeanDefinitionReaderUtils.doScanBasePackageRegisterBeanDefinitions(location, registry);
    }
}
