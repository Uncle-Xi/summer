package com.summerframework.beans.factory.config;

import com.summerframework.beans.PropertyValue;

import java.util.List;

public interface BeanDefinition {

    int AUTOWIRE_NO = 0;

    int AUTOWIRE_BY_NAME = 0;

    String SCOPE_SINGLETON = "singleton";

    boolean isSingleton();

    boolean isLazyInit();

    String getBeanClassName();

    void setBeanClassName( String beanClassName);

    void setFactoryBeanName( String factoryBeanName);


    String getFactoryBeanName();

    void setPropertyValues(List<PropertyValue> propertyValueList);

    List<PropertyValue> getPropertyValues();

    void setAutowireMode(int autowireMode);
}
