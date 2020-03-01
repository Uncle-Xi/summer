package com.summerframework.aop.interceptor;

import com.summerframework.aop.Joinpoint;
import com.summerframework.aop.MethodInterceptor;
import com.summerframework.aop.MethodInvocation;
import com.summerframework.aop.aspectj.AbstractAspectJAdvice;

import java.lang.reflect.Method;

/**
 * @description: MethodBeforeAdviceInterceptor
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class MethodBeforeAdviceInterceptor extends AbstractAspectJAdvice implements MethodInterceptor {

    private Joinpoint joinPoint;

    public MethodBeforeAdviceInterceptor(Method aspectJAdviceMethod, Object aspectTarget) {
        super(aspectJAdviceMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        this.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }

    private void before(Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint, null,null);
    }
}
