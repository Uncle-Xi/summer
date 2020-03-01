package com.summerframework.context.support;

import com.summerframework.context.ApplicationContext;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext {

    public AbstractXmlApplicationContext(){}
    public AbstractXmlApplicationContext(ApplicationContext parent) {
        super(parent);
    }

    @Override
    public void afterPropertiesSet() throws Exception { }
}
