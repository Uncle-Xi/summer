package com.summerframework.aop.framework;

public interface AopProxy {

    void setProxy(Object object);

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
