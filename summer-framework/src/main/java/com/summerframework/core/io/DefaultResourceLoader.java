package com.summerframework.core.io;

import com.summerframework.core.util.ClassUtils;
import com.summerframework.core.util.StringUtils;

/**
 * @description: DefaultResourceLoader
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DefaultResourceLoader implements ResourceLoader {

    private ClassLoader classLoader;

    public DefaultResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    @Override
    public Resource getResource(String location) {
        //如果是类路径的方式，那需要使用ClassPathResource 来得到bean 文件的资源对象
        if (location.startsWith("/")) {
            return getResourceByPath(location);
        }
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
        }
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }


    protected Resource getResourceByPath(String path) {
        return new ClassPathContextResource(path, getClassLoader());
    }


    /**
     * ClassPathResource that explicitly expresses a context-relative path
     * through implementing the ContextResource interface.
     */
    protected static class ClassPathContextResource extends ClassPathResource {

        public ClassPathContextResource(String path,  ClassLoader classLoader) {
            super(path, classLoader);
        }

        public String getPathWithinContext() {
            return getPath();
        }

        public Resource createRelative(String relativePath) {
            String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
            return new ClassPathContextResource(pathToUse, getClassLoader());
        }
    }

}
