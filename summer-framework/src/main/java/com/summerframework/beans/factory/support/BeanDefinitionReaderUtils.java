package com.summerframework.beans.factory.support;

import com.summerframework.beans.PropertyValue;
import com.summerframework.beans.factory.config.BeanDefinition;
import com.summerframework.beans.factory.config.BeanDefinitionHolder;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.logger.PrintColor;
import com.summerframework.core.util.ClassUtils;
import com.summerframework.core.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * @description: BeanDefinitionReaderUtils
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class BeanDefinitionReaderUtils {

    private static final LogFactory logger = new LogFactory(BeanDefinitionReaderUtils.class);

    public static final String AUTO_SCAN_CLASS_ANNOTATION = "auto_scan_class_annotation";
    public static final Set<String> SCAN_CLASS = new HashSet<>(128);

    public static final Set<String> SCAN_PACKAGES = new HashSet<>(16);
    public static final Set<String> LOCAL_CLASS = new HashSet<>(128);
    public static final Set<String> FACTORIES_CLASS = new HashSet<>(128);


    public static void registerBeanDefinition(
            BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
            throws RuntimeException {
        String[] beanName = definitionHolder.getAliases();
        if (beanName != null) {
            Stream.of(beanName).forEach(x -> {
                if (x != null && !x.trim().equalsIgnoreCase(StringUtils.NONE_SPACE)) {
                    registry.registerBeanDefinition(x, definitionHolder.getBeanDefinition());
                }
            });
        }
        registry.registerBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
    }

    public static void registerEarlyBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        registry.registerEarlyBeanDefinition(definitionHolder.getBeanName(), definitionHolder.getBeanDefinition());
    }

    public static void registerClassAnnotationDefinition(BeanDefinitionRegistry registry,
                                                         Class<?>... annotatedClasses) {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(AUTO_SCAN_CLASS_ANNOTATION);
        if (annotatedClasses == null) {
            annotatedClasses = AnnotationConfigUtils.CLASS_ANNOTATIONS;
        }
        beanDefinition.setPropertyValues(getPropertyValues(annotatedClasses));
        registerBeanDefinition(new BeanDefinitionHolder(beanDefinition, AUTO_SCAN_CLASS_ANNOTATION, null), registry);
    }

    public static List<PropertyValue> getPropertyValues(Class<?>... annotatedClasses) {
        List<PropertyValue> propertyValueList = new ArrayList<>();
        Stream.of(annotatedClasses).forEach(x -> propertyValueList.add(getAnnotationPropertyValue(x)));
        return propertyValueList;
    }

    public static PropertyValue getAnnotationPropertyValue(Class<?> clazz) {
        return new PropertyValue(null, clazz);
    }

    public static void doScanBasePackageRegisterBeanDefinitions(String basePackage, BeanDefinitionRegistry registry) {
        logger.info("basePackage = [" + basePackage + "].");
        doScanClass(basePackage, SCAN_CLASS);
        logger.info("SCAN_CLASS.size() -> " + SCAN_CLASS.size());
        doRegister(registry, SCAN_CLASS);
    }

    public static Set<String> scanPackages(String... packages) {
        assert packages == null;
        Stream.of(packages).forEach(p -> {
            doScanClass(p, SCAN_CLASS);
            SCAN_PACKAGES.add(p);
        });
        return SCAN_CLASS;
    }

    public static Set<String> scanPackages(List<String> packages) {
        Set<String> classSet = new HashSet<>(4);
        packages.stream().forEach(p -> {
            doScanClass(p, classSet);
            SCAN_PACKAGES.add(p);
        });
        return classSet;
    }


    protected static void doScanLocal(String basePackage, Set<String> classList){
        // StringUtils.FOLDER_SEPARATOR +
        URL url = ClassUtils.getDefaultClassLoader().getResource(
                basePackage.replaceAll("\\.", StringUtils.FOLDER_SEPARATOR));
        if (url == null) { return; }
        File classPath = new File(url.getFile());
        if (classPath == null) { return; }
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanLocal(basePackage + StringUtils.CURRENT_PATH + file.getName(), classList);
            } else {
                if (!file.getName().endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                    continue;
                }
                addClass(basePackage, classList, file.getName());
            }
        }
    }

    private static void addClass(String basePackage, Set<String> classList, String fileName){
        String className = basePackage + StringUtils.CURRENT_PATH
                + fileName
                .replace(ClassUtils.CLASS_FILE_SUFFIX, StringUtils.NONE_SPACE);
        classList.add(className);
    }


    public static void doScanClass(String basePackage, Set<String> classList) {
        try {
            logger.info("doScanClass basePackage -> " + basePackage);
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
                    .getResources(basePackage.replace(".", StringUtils.FOLDER_SEPARATOR));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                logger.info("doScanClass URL url -> " + url);
                String protocol = url.getProtocol();
                logger.info("doScanClass protocol -> " + protocol);
                if ("jar".equalsIgnoreCase(protocol)) {
                    PrintColor.setIsJar(true);
                    doScanJar(basePackage, url, classList);
                } else {
                    doScanLocal(basePackage, classList);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void doScanJar(String basePackage, URL url, Set<String> classList) throws IOException {
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        if (connection != null) {
            JarFile jarFile = connection.getJarFile();
            if (jarFile != null) {
                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                while (jarEntryEnumeration.hasMoreElements()) {
                    JarEntry entry = jarEntryEnumeration.nextElement();
                    String jarEntryName = entry.getName();
                    if (!jarEntryName.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) { continue; }
                    boolean isServiceClass = jarEntryName.contains(ClassUtils.CLASS_FILE_SUFFIX)
                            && jarEntryName.replaceAll(StringUtils.FOLDER_SEPARATOR, StringUtils.CURRENT_PATH)
                            .startsWith(basePackage);
                    if (!isServiceClass)                                      { continue; }
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(StringUtils.CURRENT_PATH))
                            .replaceAll(StringUtils.FOLDER_SEPARATOR, StringUtils.CURRENT_PATH);
                    logger.info("doScanJar className -> " + className);
                    classList.add(className);
                }
            }
        }
    }

    protected static void doRegister(BeanDefinitionRegistry registry, Set<String> registerClass) {
        try {
            if (registerClass == null) {
                return;
            }
            for (String className : registerClass) {
                BeanDefinition beanDefinition = new GenericBeanDefinition();
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()
                        || !AnnotationConfigUtils.isContentAnnotation(beanClass, AnnotationConfigUtils.CLASS_ANNOTATIONS)) {
                    continue;
                }
                beanDefinition.setBeanClassName(className);
                beanDefinition.setFactoryBeanName(className);
                logger.info("doRegister className = [" + className + "].");
                if (AnnotationConfigUtils
                        .isContentAnnotation(beanClass, AnnotationConfigUtils.ASPECT_CLASS_ANNOTATIONS)) {
                    registerEarlyBeanDefinition(
                            new BeanDefinitionHolder(beanDefinition, beanDefinition.getBeanClassName(), null),
                            registry);
                } else {
                    registerBeanDefinition(initBeanDefinitionHolder(
                            beanDefinition, className, getBeanAliases(beanClass)), registry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static String[] getBeanAliases(Class<?> beanClass) {
        List<String> aliases = new ArrayList<>(3);
        Class<?>[] interfaces = beanClass.getInterfaces();
        String defaultBeanName;
        for (Class<?> i : interfaces) {
            aliases.add(i.getName());
            defaultBeanName = StringUtils.getDefaultClassName(i.getName());
            if (defaultBeanName != null) {
                aliases.add(defaultBeanName);
            }
        }
        //aliases.add(beanClass.getName());
        defaultBeanName = StringUtils.getDefaultClassName(beanClass.getName());
        if (defaultBeanName != null) {
            aliases.add(defaultBeanName);
        }
        return aliases.toArray(new String[aliases.size()]);
    }

    protected static BeanDefinitionHolder initBeanDefinitionHolder(BeanDefinition beanDefinition,
                                                                   String beanName,
                                                                   String[] aliases) {
        BeanDefinitionHolder definitionHolder =
                new BeanDefinitionHolder(beanDefinition, beanName, aliases);
        return definitionHolder;
    }

    public static void registerLocal(Set<String> locals) {
        LOCAL_CLASS.addAll(locals);
    }

    public static void registerFactories(Set<String> factories) {
        FACTORIES_CLASS.addAll(factories);
    }
}
