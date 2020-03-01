package com.summerframework.beans;

import com.summerframework.core.util.Assert;

/**
 * @description: BeanUtils
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class BeanUtils {

    public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
        Assert.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            throw new BeanInstantiationException(clazz, "Specified class is an interface");
        }
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw new BeanInstantiationException(clazz, "No default constructor found", ex);
        }
    }
}
