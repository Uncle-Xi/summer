package com.summerframework.core.io.support;

import com.summerframework.context.annotation.ImportSelector;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.Assert;
import com.summerframework.core.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @description: ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class SummerFactoriesLoader {

    private static final LogFactory logger = new LogFactory(SummerFactoriesLoader.class);

    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/summer.factories";

    private static final Map<ClassLoader, Map<String, List<String>>> cache = new ConcurrentHashMap<>();

    public static <T> List<T> loadFactories(Class<T> factoryClass,  ClassLoader classLoader) {
        Assert.notNull(factoryClass, "'factoryClass' must not be null");
        ClassLoader classLoaderToUse = classLoader;
        if (classLoaderToUse == null) {
            classLoaderToUse = SummerFactoriesLoader.class.getClassLoader();
        }
        Set<String> factoryNames = loadFactoryNames(factoryClass, classLoaderToUse);
        logger.info("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
        List<T> result = new ArrayList<>(factoryNames.size());
        for (String factoryName : factoryNames) {
            result.add(instantiateFactory(factoryName, factoryClass, classLoaderToUse));
        }
        return result;
    }

    public static Set<String> loadFactories( ClassLoader classLoader){
        if (classLoader == null) {
            classLoader = getClassLoader();
        }
        return loadSummerFactories(classLoader);
    }


    public static ClassLoader getClassLoader(){
        return SummerFactoriesLoader.class.getClassLoader();
    }

    public static Set<String> loadFactoryNames(Class<?> factoryClass,  ClassLoader classLoader) {
        //String factoryClassName = factoryClass.getName();
        //return loadSummerFactories(classLoader).getOrDefault(factoryClassName, Collections.emptyList());
        return loadSummerFactories(classLoader);
    }

    protected static Set<String> loadSummerFactories( ClassLoader classLoader) {
        Set<String> result = new HashSet<>();
        try {
            String selectKey = ImportSelector.AUTO_CONFIGURATION_CLASS_PREFIX;
            Enumeration<URL> urls = (classLoader != null ?
                    classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                    ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                logger.info("summer.factories url >>> " + url.getPath());
                Properties properties = ConfigurableEnvironment.loadProperties(url);
                String enabled = properties.getProperty(selectKey);
                if (StringUtils.isEmpty(enabled)) {
                    continue;
                }
                Stream.of(enabled.split(StringUtils.COMMA)).forEach(e -> {
                    result.add(e.trim());
                });
            }
            return result;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to load factories from location [" +
                    FACTORIES_RESOURCE_LOCATION + "]", ex);
        }
    }


//    private static Map<String, List<String>> loadSummerFactories( ClassLoader classLoader) {
//        Map<String, List<String>> result = cache.get(classLoader);
//        if (result != null) {
//            return result;
//        }
//        result = new ConcurrentHashMap<>();
//        try {
//            String selectKey = ImportSelector.AUTO_CONFIGURATION_CLASS_PREFIX;
//            Enumeration<URL> urls = (classLoader != null ?
//                    classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
//                    ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
//            List<String> enabledList = new ArrayList<>();
//            while (urls.hasMoreElements()) {
//                URL url = urls.nextElement();
//                logger.info("url.getPath() >>>> " + url.getPath());
//                Properties properties = ConfigurableEnvironment.loadProperties(url);
//                String enabled = properties.getProperty(selectKey);
//                if (StringUtils.isEmpty(enabled)) {
//                    continue;
//                }
//                Stream.of(enabled.split(StringUtils.COMMA)).forEach(e -> {
//                    enabledList.add(e.trim());
//                });
//            }
//            result.put(ImportSelector.class.toString(), enabledList);
//            cache.put(classLoader, result);
//            return result;
//        }
//        catch (IOException ex) {
//            throw new IllegalArgumentException("Unable to load factories from location [" +
//                    FACTORIES_RESOURCE_LOCATION + "]", ex);
//        }
//    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiateFactory(String instanceClassName, Class<T> factoryClass, ClassLoader classLoader) {
        try {
            Class<?> instanceClass = classLoader.loadClass(instanceClassName);
            if (!factoryClass.isAssignableFrom(instanceClass)) {
                throw new IllegalArgumentException(
                        "Class [" + instanceClassName + "] is not assignable to [" + factoryClass.getName() + "]");
            }
            return (T) instanceClass.newInstance();
        }
        catch (Throwable ex) {
            throw new IllegalArgumentException("Unable to instantiate factory class: " + factoryClass.getName(), ex);
        }
    }
}
