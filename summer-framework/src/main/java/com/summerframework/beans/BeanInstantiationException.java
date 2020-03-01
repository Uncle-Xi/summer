package com.summerframework.beans;

import com.summerframework.core.NestedRuntimeException;

/**
 * @description: BeanInstantiationException
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class BeanInstantiationException extends NestedRuntimeException {
    public BeanInstantiationException(String msg) {
        super(msg);
    }

    public BeanInstantiationException(Class<?> beanClass, String msg) {
        this(beanClass, msg, null);
    }

    /**
     * Create a new BeanInstantiationException.
     * @param beanClass the offending bean class
     * @param msg the detail message
     * @param cause the root cause
     */
    public BeanInstantiationException(Class<?> beanClass, String msg,  Throwable cause) {
        super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
    }
}
