package com.summerframework.context.annotation;

public interface AnnotationConfigRegistry {

    void register(Class<?>... annotatedClasses);

    void scan(String... basePackages);
}
