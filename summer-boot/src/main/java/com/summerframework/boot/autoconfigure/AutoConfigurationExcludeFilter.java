package com.summerframework.boot.autoconfigure;

import com.summerframework.core.type.TypeFilter;

import java.io.IOException;

/**
 * @description: AutoConfigurationExcludeFilter
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AutoConfigurationExcludeFilter implements TypeFilter {
    @Override
    public boolean match(String matcher, Object object) throws IOException {
        return false;
    }
}
