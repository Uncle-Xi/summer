package com.summerframework.core.config;

import com.summerframework.beans.BeansException;

public interface BeanPostProcessor {


    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
