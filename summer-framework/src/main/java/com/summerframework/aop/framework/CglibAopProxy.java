package com.summerframework.aop.framework;

import com.summerframework.aop.MethodInvocation;
import com.summerframework.core.logger.LogFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @description: CglibAopProxy
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class CglibAopProxy implements AopProxy, MethodInterceptor, Serializable {

    private static final LogFactory logger = new LogFactory(CglibAopProxy.class);
    private AdvisedSupport advised;

    public CglibAopProxy(AdvisedSupport advised){
        this.advised = advised;
    }

    @Override
    public void setProxy(Object object) {
        advised.setTarget(object);
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (this.advised.advisedIsEmpty()) {
            return advised.getTarget();
        }
        logger.info("CglibAopProxy getProxy ...");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetClass());
        enhancer.setCallback(this);
        return getObjects(advised.getTarget(), enhancer);
    }

    private Object getObjects(Object target, Enhancer enhancer){
        Class<?> clazz = target.getClass();
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Constructor mpConstructor = null;
        int parmCnt = 0;
        for (Constructor constructor : constructors) {
            int pc = constructor.getParameterCount();
            if (pc == 0) {
                return enhancer.create();
            }
            if (pc > parmCnt) {
                parmCnt = pc;
                mpConstructor = constructor;
            }
        }
        mpConstructor.setAccessible(true);
        Class<?>[] parameterTypes = mpConstructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        return enhancer.create(parameterTypes, args);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers =
                this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());
        logger.info("CglibAopProxy invoke ...");
        MethodInvocation invocation = new ReflectiveMethodInvocation(
                proxy,
                this.advised.getTarget(),
                method,
                args,
                this.advised.getTargetClass(),
                interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
