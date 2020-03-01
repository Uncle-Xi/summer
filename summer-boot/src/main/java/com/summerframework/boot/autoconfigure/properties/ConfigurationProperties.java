package com.summerframework.boot.autoconfigure.properties;

import com.summerframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigurationProperties {

    @AliasFor("prefix")
    String value() default "";

    @AliasFor("value")
    String prefix() default "";

    boolean ignoreInvalidFields() default false;

    boolean ignoreUnknownFields() default true;

}
