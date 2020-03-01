package com.summerframework.core.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AliasFor {

    /**
     * Alias for {@link #attribute}.
     * <p>Intended to be used instead of {@link #attribute} when {@link #annotation}
     * is not declared &mdash; for example: {@code @AliasFor("value")} instead of
     * {@code @AliasFor(attribute = "value")}.
     */
    @AliasFor("attribute")
    String value() default "";

    @AliasFor("value")
    String attribute() default "";

    Class<? extends Annotation> annotation() default Annotation.class;
}
