package com.summerframework.aop.framework;

import com.summerframework.aop.MethodInvocation;
import com.summerframework.core.logger.LogFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @description: JdkDynamicAopProxy
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class JdkDynamicAopProxy  implements AopProxy, InvocationHandler, Serializable {

    private static final LogFactory logger = new LogFactory(JdkDynamicAopProxy.class);
    private AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised){
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public void setProxy(Object object) {
        advised.setTarget(object);
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (this.advised.advisedIsEmpty()) {
            return advised.getTarget();
        }
        logger.info("JdkDynamicAopProxy getProxy ...");
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers =
                this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());
        logger.info("JdkDynamicAopProxy invoke ...");
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
