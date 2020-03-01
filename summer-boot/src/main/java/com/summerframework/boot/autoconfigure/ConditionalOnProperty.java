package com.summerframework.boot.autoconfigure;

import com.summerframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Conditional(OnPropertyCondition.class)
public @interface ConditionalOnProperty {

    String[] value() default {};

    String prefix() default "";

    String[] name() default {};

    String havingValue() default "";

    boolean matchIfMissing() default false;

}