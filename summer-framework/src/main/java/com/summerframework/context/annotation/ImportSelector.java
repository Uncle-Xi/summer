package com.summerframework.context.annotation;

import com.summerframework.core.type.AnnotationMetadata;

public interface ImportSelector {

    String AUTO_CONFIGURATION_CLASS_PREFIX
            = "org.summerframework.boot.autoconfigure.EnableAutoConfiguration";


    String[] selectImports(AnnotationMetadata importingClassMetadata);
}
