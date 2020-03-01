package com.summerframework.aop.aspectj;

import com.summerframework.aop.Advice;
import com.summerframework.aop.Joinpoint;
import com.summerframework.core.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @description: AbstractAspectJAdvice
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class AbstractAspectJAdvice implements Advice, Serializable {

    protected Method aspectJAdviceMethod;

    protected Object aspectTarget;

    public AbstractAspectJAdvice(Method aspectJAdviceMethod, Object aspectTarget) {
        Assert.notNull(aspectJAdviceMethod, "Advice method must not be null");
        this.aspectJAdviceMethod = aspectJAdviceMethod;
        this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod( Joinpoint jpMatch,
                                         Object returnValue,  Throwable ex)
            throws Throwable {
        Class<?>[] paramTypes = this.aspectJAdviceMethod.getParameterTypes();
        if (null == paramTypes || paramTypes.length == 0) {
            return this.aspectJAdviceMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                if (paramTypes[i] == Joinpoint.class) {
                    args[i] = jpMatch;
                } else if (paramTypes[i] == Throwable.class) {
                    args[i] = ex;
                } else if (paramTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return this.aspectJAdviceMethod.invoke(aspectTarget, args);
        }
    }
}
