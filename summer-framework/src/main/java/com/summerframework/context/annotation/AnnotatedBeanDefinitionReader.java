package com.summerframework.context.annotation;

import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.beans.factory.support.BeanDefinitionRegistry;
import com.summerframework.core.env.Environment;

import java.util.Set;

/**
 * @description: AnnotatedBeanDefinitionReader
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AnnotatedBeanDefinitionReader {

    private final BeanDefinitionRegistry registry;
    private Environment environment;

    public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry registry, Environment environment) {
        this.registry = registry;
        this.environment = environment;
    }

    public void register(Class<?>... annotatedClasses) {
        BeanDefinitionReaderUtils.registerClassAnnotationDefinition(registry, annotatedClasses);
    }

    public void registerLocal(Set<String> locals) {
        BeanDefinitionReaderUtils.registerLocal(locals);
    }

    public void registerFactories(Set<String> factories ) {
        BeanDefinitionReaderUtils.registerFactories(factories);
    }
}
