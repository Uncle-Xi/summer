package com.summerframework.core.io;

import com.summerframework.core.util.ResourceUtils;

public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

    Resource getResource(String location);


    ClassLoader getClassLoader();
}
