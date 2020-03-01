package com.summer.xirpc.configuration;

import com.summerframework.context.annotation.ImportSelector;
import com.summerframework.core.type.AnnotationMetadata;

/**
 * @description: XiRpcImportSelector
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XiRpcImportSelector implements ImportSelector {

    private static String[] selector = {"com.xirpc.config.ServiceBean"};

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return selector;
    }
}
