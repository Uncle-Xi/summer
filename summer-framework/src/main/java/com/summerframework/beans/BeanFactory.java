package com.summerframework.beans;


public interface BeanFactory {

    String FACTORY_BEAN_PREFIX = "&";

    Object getBean(String name) throws BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    String[] getBeanDefinitionNames();

    boolean containsBean(String name);

    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;
}
