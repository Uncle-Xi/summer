package com.summerframework.beans;

import com.summerframework.core.config.BeanPostProcessor;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {

    void beforeProcessBeanFactory();

    void postProcessEarlyBeanFactory();

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
