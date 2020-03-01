package com.summerframework.context;

import com.summerframework.beans.BeanFactory;
import com.summerframework.beans.BeansException;
import com.summerframework.beans.NoSuchBeanDefinitionException;
import com.summerframework.beans.factory.config.BeanDefinition;
import com.summerframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.summerframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import com.summerframework.beans.factory.support.BeanDefinitionRegistry;
import com.summerframework.beans.factory.support.GenericBeanDefinition;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @description: DefaultListableBeanFactory
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {

    private static final LogFactory logger = new LogFactory(DefaultListableBeanFactory.class);


    public DefaultListableBeanFactory() {
        super();
    }

    public DefaultListableBeanFactory( BeanFactory parentBeanFactory) {
        //super(parentBeanFactory);
    }

    public void registerBeanAndDefinition(String beanName, Object object){
        this.singletonObjects.put(beanName, object);
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setFactoryBeanName(beanName);
        beanDefinition.setBeanClassName(beanName);
        logger.info(": registerBeanAndDefinition: " + beanName);
        this.beanDefinitionNames.add(beanName);
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws RuntimeException {
        this.beanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanDefinition.getBeanClassName());
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.beanDefinitionMap.get(beanName);
        if (bd == null) {
            logger.info("No bean named '" + beanName + "' found in " + this);
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return bd;
    }

    @Override
    public void registerEarlyBeanDefinition(String beanName, BeanDefinition beanDefinition) throws RuntimeException {
        this.earlyBeanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return StringUtils.toStringArray(this.beanDefinitionNames);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public boolean containsBean(String name) {
        return this.singletonObjects.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return true;
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        Object bean = this.singletonObjects.get(name);
        return bean.getClass();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = this.singletonObjects.get(name);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(name);
        if (beanDefinition != null) {
            bean = this.singletonObjects.get(beanDefinition.getBeanClassName());
            if (bean != null) {
                System.out.printf("getBean name = [%s]; getBeanClassName = [%s].\n", name, beanDefinition.getBeanClassName());
                return bean;
            }
        } else {
            beanDefinition = new GenericBeanDefinition();
            beanDefinition.setFactoryBeanName(name);
            beanDefinition.setBeanClassName(name);
            logger.info("BeanDefinition is null Bean name -> " + name);
        }
        return doGetBean(beanDefinition);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        // TODO
        // return getBean();
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        Object bean = getBean(name);
        if (bean.getClass() == requiredType) {
            return (T) bean;
        }
        throw new UnsupportedOperationException("expected single matching bean but found");
    }

    @Override
    public Map<String, BeanDefinition> getEarlyBeanDefinitionMap() {
        return earlyBeanDefinitionMap;
    }
}
