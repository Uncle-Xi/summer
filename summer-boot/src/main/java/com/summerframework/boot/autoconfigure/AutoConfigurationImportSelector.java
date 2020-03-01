package com.summerframework.boot.autoconfigure;

import com.summerframework.context.annotation.ImportSelector;
import com.summerframework.core.type.AnnotationMetadata;

/**
 * @description: AutoConfigurationImportSelector
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AutoConfigurationImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[0];
    }
}
