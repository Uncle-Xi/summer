package com.summerframework.core.io;

import com.summerframework.core.util.Assert;
import com.summerframework.core.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @description: ClassPathResource
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ClassPathResource implements Resource {

    private final String path;

    public final static String defaultPropertiesPath = "application.properties";

    public final static String PROPERTIES_NAME = "application.properties";


    private ClassLoader classLoader;


    private Class<?> clazz;

    private InputStream inputStream;

    public final String getPath() {
        return this.path;
    }

    public ClassPathResource(String path,  ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        //System.out.printf("ClassPathResource path = [%s].\n", path);
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
        this.inputStream = classLoader.getResourceAsStream(path);
    }

    public final ClassLoader getClassLoader() {
        return (this.clazz != null ? this.clazz.getClassLoader() : this.classLoader);
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public URL getURL() throws IOException {
        return null;
    }

    @Override
    public File getFile() throws IOException {
        return null;
    }

    @Override
    public String getFilename() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.inputStream;
    }
}
