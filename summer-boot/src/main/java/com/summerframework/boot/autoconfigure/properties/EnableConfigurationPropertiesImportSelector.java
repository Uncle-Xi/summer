package com.summerframework.boot.autoconfigure.properties;

import com.summerframework.context.annotation.ImportSelector;
import com.summerframework.core.type.AnnotationMetadata;

/**
 * @description: EnableConfigurationPropertiesImportSelector
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class EnableConfigurationPropertiesImportSelector implements ImportSelector {

    private static final String[] IMPORTS = { };

    @Override
    public String[] selectImports(AnnotationMetadata metadata) {
        return IMPORTS;
    }

}
