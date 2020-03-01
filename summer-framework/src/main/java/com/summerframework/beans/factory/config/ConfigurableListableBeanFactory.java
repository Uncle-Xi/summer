package com.summerframework.beans.factory.config;

import com.summerframework.beans.ConfigurableBeanFactory;
import com.summerframework.context.ListableBeanFactory;

import java.util.Map;

public interface ConfigurableListableBeanFactory extends AutowireCapableBeanFactory, ListableBeanFactory, ConfigurableBeanFactory {

    Map<String, BeanDefinition> getEarlyBeanDefinitionMap();
}
