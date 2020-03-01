package com.summerframework.context.support;

import com.summerframework.beans.BeansException;
import com.summerframework.context.DefaultListableBeanFactory;

import java.io.IOException;

public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException { }
}
