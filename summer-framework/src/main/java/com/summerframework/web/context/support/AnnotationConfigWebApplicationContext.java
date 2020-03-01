package com.summerframework.web.context.support;

import com.summerframework.beans.BeansException;
import com.summerframework.context.DefaultListableBeanFactory;
import com.summerframework.context.annotation.AnnotatedBeanDefinitionReader;
import com.summerframework.context.annotation.AnnotationConfigRegistry;
import com.summerframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class AnnotationConfigWebApplicationContext extends AbstractRefreshableWebApplicationContext
        implements AnnotationConfigRegistry {

    private final Set<Class<?>> annotatedClasses = new LinkedHashSet<>();
    private final Set<String> basePackages = new LinkedHashSet<>();

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        AnnotatedBeanDefinitionReader reader = getAnnotatedBeanDefinitionReader(beanFactory);
        ClassPathBeanDefinitionScanner scanner = getClassPathBeanDefinitionScanner(beanFactory);
        reader.register(this.annotatedClasses.toArray(new Class<?>[this.annotatedClasses.size()]));
        scanner.scan(this.basePackages.toArray(new String[this.basePackages.size()]));
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                //PropertyAccessorUtils.initProporties();
            }
        }
    }

    @Override
    public void register(Class<?>... annotatedClasses) {
        this.annotatedClasses.addAll(Arrays.asList(annotatedClasses));
    }

    @Override
    public void scan(String... basePackages) {
        this.basePackages.addAll(Arrays.asList(basePackages));
    }

    protected AnnotatedBeanDefinitionReader getAnnotatedBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        return new AnnotatedBeanDefinitionReader(beanFactory, getEnvironment());
    }

    protected ClassPathBeanDefinitionScanner getClassPathBeanDefinitionScanner(DefaultListableBeanFactory beanFactory) {
        return new ClassPathBeanDefinitionScanner(beanFactory, getEnvironment());
    }
}
