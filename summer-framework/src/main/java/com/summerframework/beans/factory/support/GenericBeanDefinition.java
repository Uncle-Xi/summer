package com.summerframework.beans.factory.support;

import com.summerframework.beans.PropertyValue;
import com.summerframework.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition, Cloneable {

    private boolean lazyInit = false;

    private boolean singleton = false;

    private volatile Object beanClass;

    private int autowireMode = AUTOWIRE_NO;

    private List<PropertyValue> propertyValueList;


    private String factoryBeanName;

    @Override
    public boolean isSingleton() {
        return this.lazyInit;
    }

    @Override
    public boolean isLazyInit() {
        return this.singleton;
    }

    @Override
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class) {
            return ((Class<?>) beanClassObject).getName();
        }
        else {
            return (String) beanClassObject;
        }
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }

    @Override
    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    @Override
    public void setPropertyValues(List<PropertyValue> propertyValueList) {
        this.propertyValueList = propertyValueList;
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        if (this.propertyValueList == null) {
            this.propertyValueList = new ArrayList<>();
        }
        return this.propertyValueList;
    }

    public int getAutowireMode() {
        return this.autowireMode;
    }

    @Override
    public void setFactoryBeanName( String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override

    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }
}
