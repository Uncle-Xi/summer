package com.summerframework.context.annotation;

import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.beans.factory.support.BeanDefinitionRegistry;
import com.summerframework.core.env.Environment;

import java.util.Set;
import java.util.stream.Stream;

/**
 * @description: ClassPathBeanDefinitionScanner
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ClassPathBeanDefinitionScanner {

    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, Environment environment) {
        this.registry = registry;
    }

    public void scan(String... basePackages) {
        assert basePackages == null;
        Stream.of(basePackages).forEach(x ->
                BeanDefinitionReaderUtils.doScanBasePackageRegisterBeanDefinitions(x, registry));
    }

    public Set<String> scanPackages(String... basePackages) {
        return BeanDefinitionReaderUtils.scanPackages(basePackages);
    }
}
