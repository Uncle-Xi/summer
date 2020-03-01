package com.summerframework.context.annotation;

import com.summerframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    @AliasFor(annotation = Component.class)
    String value() default "";

}