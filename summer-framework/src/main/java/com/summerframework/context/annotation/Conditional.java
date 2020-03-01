package com.summerframework.context.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {

    /**
     * All {@link Condition}s that must {@linkplain Condition#matches match}
     * in order for the component to be registered.
     */
    Class<? extends Condition>[] value();

}