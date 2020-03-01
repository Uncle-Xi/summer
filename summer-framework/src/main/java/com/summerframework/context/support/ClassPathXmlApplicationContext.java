package com.summerframework.context.support;

import com.summerframework.beans.BeansException;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.DefaultListableBeanFactory;

import java.io.IOException;

/**
 * @description: ClassPathXmlApplicationContext
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {


    public ClassPathXmlApplicationContext(ApplicationContext parent) {
        super(parent);
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {

    }
}
