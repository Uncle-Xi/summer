package com.summerframework.aop.framework;

import com.summerframework.aop.MethodInterceptor;
import com.summerframework.aop.MethodInvocation;
import com.summerframework.core.logger.LogFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: ReflectiveMethodInvocation
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ReflectiveMethodInvocation implements MethodInvocation, Cloneable {

    private static final LogFactory logger = new LogFactory(ReflectiveMethodInvocation.class);

    protected final Object proxy;
    protected final Object target;
    protected final Method method;
    protected Object[] arguments;
    private final Class<?> targetClass;
    private Map<String, Object> userAttributes;
    protected final List<?> interceptorsAndDynamicMethodMatchers;
    private int currentInterceptorIndex = -1;

    protected ReflectiveMethodInvocation(
            Object proxy,  Object target, Method method,  Object[] arguments,
             Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments/*AopProxyUtils.adaptArgumentsIfNecessary(method, arguments)*/;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override

    public Object proceed() throws Throwable {
        //System.out.println("[currentInterceptorIndex] - [" + currentInterceptorIndex);
        //System.out.println("[interceptorsAndDynamicMethodMatchers] - [" + interceptorsAndDynamicMethodMatchers);
        //System.out.println("[interceptorsAndDynamicMethodMatchers] - [" + interceptorsAndDynamicMethodMatchers.size());
        if (this.interceptorsAndDynamicMethodMatchers == null) {
            logger.info("【ReflectiveMethodInvocation】【proceed】【null chain】");
            return invokeJoinpoint();
        }
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return invokeJoinpoint();
        }
        Object interceptorOrInterceptionAdvice =
                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if (interceptorOrInterceptionAdvice instanceof MethodInterceptor) {
            return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
        } else {
            return proceed();
        }
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (this.userAttributes == null) {
                this.userAttributes = new HashMap<>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (this.userAttributes != null) {
                this.userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (this.userAttributes != null ? this.userAttributes.get(key) : null);
    }


    protected Object invokeJoinpoint() throws Throwable {
        return this.method.invoke(this.target, this.arguments);
    }
}
