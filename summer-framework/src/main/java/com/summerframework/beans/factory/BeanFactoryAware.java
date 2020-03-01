package com.summerframework.beans.factory;

import com.summerframework.beans.Aware;
import com.summerframework.beans.BeanFactory;
import com.summerframework.beans.BeansException;

public interface BeanFactoryAware extends Aware {


    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}

