package com.summerframework.boot.autoconfigure;

import com.summerframework.context.annotation.ComponentScan;
import com.summerframework.context.annotation.Configuration;
import com.summerframework.context.annotation.FilterType;
import com.summerframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SummerBootApplication {

    @AliasFor(annotation = EnableAutoConfiguration.class)
    Class<?>[] exclude() default {};

    @AliasFor(annotation = EnableAutoConfiguration.class)
    String[] excludeName() default {};

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] scanBasePackages() default {};

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};

}
