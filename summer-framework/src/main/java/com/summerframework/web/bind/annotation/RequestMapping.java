package com.summerframework.web.bind.annotation;

import com.summerframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping {

    String name() default "";

    @AliasFor("path")
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};

    String[] params() default {};

    RequestMethod[] method() default {};
}
