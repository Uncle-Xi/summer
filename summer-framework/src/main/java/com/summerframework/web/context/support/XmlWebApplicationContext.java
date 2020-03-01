package com.summerframework.web.context.support;

import com.summerframework.beans.BeansException;
import com.summerframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.summerframework.context.DefaultListableBeanFactory;

import java.io.IOException;

/**
 * @description: XmlWebApplicationContext
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext {

    public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";

    public static final String DEFAULT_BASE_PACKAGE = "summer.base.package";

    public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";

    public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.setResourceLoader(this);
        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException {
        reader.loadBeanDefinitions(getEnvironment().getProperty(DEFAULT_BASE_PACKAGE));
    }

    @Override
    protected String[] getDefaultConfigLocations() {
        if (getNamespace() != null) {
            return new String[] {DEFAULT_CONFIG_LOCATION_PREFIX + getNamespace() + DEFAULT_CONFIG_LOCATION_SUFFIX};
        }
        else {
            return new String[] {DEFAULT_CONFIG_LOCATION};
        }
    }

}
