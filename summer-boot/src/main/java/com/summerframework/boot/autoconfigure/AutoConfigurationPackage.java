package com.summerframework.boot.autoconfigure;

import com.summerframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoConfigurationPackages.class)
public @interface AutoConfigurationPackage {

}
