package com.summerframework.beans.factory.support;

import com.summerframework.beans.NoSuchBeanDefinitionException;
import com.summerframework.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws RuntimeException;

    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    void registerEarlyBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws RuntimeException;

    String[] getBeanDefinitionNames();
}
