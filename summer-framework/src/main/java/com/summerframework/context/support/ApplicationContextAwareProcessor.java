package com.summerframework.context.support;

import com.summerframework.beans.BeansException;
import com.summerframework.context.ApplicationContextAware;
import com.summerframework.core.config.BeanPostProcessor;

/**
 * @description: ApplicationContextAwareProcessor
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private AbstractApplicationContext beanFactory;

    public ApplicationContextAwareProcessor(AbstractApplicationContext beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(beanFactory);
        }
        return bean;
    }
}
