package com.summerframework.web.bind.annotation;

import com.summerframework.core.annotation.AliasFor;
import com.summerframework.stereotype.Controller;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {

    @AliasFor(annotation = Controller.class)
    String value() default "";

}
