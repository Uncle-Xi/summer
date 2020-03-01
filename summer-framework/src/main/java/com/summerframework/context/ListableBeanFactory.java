package com.summerframework.context;

import com.summerframework.beans.HierarchicalBeanFactory;

public interface ListableBeanFactory extends HierarchicalBeanFactory {

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

}
