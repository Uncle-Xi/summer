package com.summerframework.boot.autoconfigure.properties;

import com.summerframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableConfigurationPropertiesImportSelector.class)
public @interface EnableConfigurationProperties {

    Class<?>[] value() default {};

}
