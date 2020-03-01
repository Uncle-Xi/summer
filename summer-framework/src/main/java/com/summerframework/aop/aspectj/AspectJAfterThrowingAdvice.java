package com.summerframework.aop.aspectj;

import com.summerframework.aop.MethodInterceptor;
import com.summerframework.aop.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @description: AspectJAfterThrowingAdvice
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    private String throwingName;

    public AspectJAfterThrowingAdvice(Method aspectJAdviceMethod, Object aspectTarget) {
        super(aspectJAdviceMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            invokeAdviceMethod(invocation, null, e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName) {
        this.throwingName = throwName;
    }
}
