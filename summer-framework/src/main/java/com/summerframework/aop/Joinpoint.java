package com.summerframework.aop;

import java.lang.reflect.Method;

public interface Joinpoint {

    Object proceed() throws Throwable;

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
