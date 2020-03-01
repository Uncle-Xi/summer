package com.summerframework.aop.aspectj;

import com.summerframework.aop.Joinpoint;
import com.summerframework.aop.MethodInterceptor;
import com.summerframework.aop.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @description: AfterReturningAdviceInterceptor
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice implements MethodInterceptor {

    private Joinpoint joinPoint;

    public AspectJAfterReturningAdvice(Method aspectJAdviceMethod, Object aspectTarget) {
        super(aspectJAdviceMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint, retVal,null);
    }
}
