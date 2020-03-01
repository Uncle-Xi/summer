package com.summerframework.boot.autoconfigure;

import com.summerframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {

    Class<?>[] value() default {};

    String[] name() default {};
}
