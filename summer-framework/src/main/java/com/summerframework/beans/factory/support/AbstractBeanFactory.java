package com.summerframework.beans.factory.support;

import com.summerframework.beans.BeansException;
import com.summerframework.beans.ConfigurableBeanFactory;
import com.summerframework.beans.factory.config.BeanDefinition;
import com.summerframework.core.config.BeanPostProcessor;
import com.summerframework.core.env.Environment;
import com.summerframework.core.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements ConfigurableBeanFactory {

    protected Environment environment;

    /** BeanPostProcessors to apply in createBean. */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /** Cache of singleton objects: bean name --> bean instance */
    protected final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    protected <T> T doGetBean(BeanDefinition beanDefinition) throws BeansException {
        return (T) createBean(beanDefinition.getBeanClassName(), beanDefinition, null);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    protected abstract Object createBean(String beanClassName, BeanDefinition beanDefinition, Object args);

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
