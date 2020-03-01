package com.summerframework.boot.autoconfigure;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//@AutoConfigurationPackage
//@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {

    String ENABLED_OVERRIDE_PROPERTY = "summer.boot.enableautoconfiguration";

    Class<?>[] exclude() default {};

    String[] excludeName() default {};

}
