package com.summerframework.boot;

import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.beans.factory.support.BeanDefinitionRegistry;
import com.summerframework.context.annotation.AnnotatedBeanDefinitionReader;
import com.summerframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.io.ResourceLoader;
import com.summerframework.core.io.support.SummerFactoriesLoader;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.logger.PrintColor;
import com.summerframework.core.util.Assert;
import com.summerframework.core.util.ClassUtils;
import com.summerframework.core.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * @description: BeanDefinitionLoader
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class BeanDefinitionLoader extends SummerFactoriesLoader {

    private static final LogFactory logger = new LogFactory(BeanDefinitionLoader.class);

    private final Object[] sources;

    public ConfigurableEnvironment environment;

    private final AnnotatedBeanDefinitionReader annotatedReader;

    private final ClassPathBeanDefinitionScanner scanner;

    private ResourceLoader resourceLoader;

    BeanDefinitionLoader(BeanDefinitionRegistry registry, ConfigurableEnvironment environment, Object... sources) {
        Assert.notNull(registry, "Registry must not be null");
        this.sources = sources;
        this.environment = environment;
        this.annotatedReader = new AnnotatedBeanDefinitionReader(registry, environment);
        this.scanner = new ClassPathBeanDefinitionScanner(registry, environment);
    }

    public void load() {
        logger.info("local - " + sources[0]);
        Set<String> locals = scanner.scanPackages((String) sources[0]);
        annotatedReader.registerLocal(locals);
        Set<String> factories = loadFactories(null);
        annotatedReader.registerFactories(factories);
        logger.info(PrintColor.getColor(1) + " locals.size() -> " + locals.size() + PrintColor.getColor(1));
        logger.info(" factories.size() -> " + factories.size());
    }

    private void display(List<String> data){
        data.stream().forEach(d -> logger.info("show - " + d));
    }


    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = BeanDefinitionLoader.class.getClassLoader();
        //Enumeration<URL> urls = (classLoader != null ?
        //        classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
        //        ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        //while (urls.hasMoreElements()) {
        //    URL url = urls.nextElement();
        //    logger.info(url.getPath());
        //}

        Object[] sources = new Object[]{BeanDefinitionLoader.class.getName()};
        String basePackage = (String) sources[0];
        logger.info(basePackage);
        basePackage = "com.acceptance";
        basePackage = "com.summerframework.boot.BeanDefinitionLoader";
        logger.info(basePackage);
        URL url = ClassUtils.getDefaultClassLoader().getResource(basePackage.replaceAll("\\.", StringUtils.FOLDER_SEPARATOR));
        //url = ClassUtils.getDefaultClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        logger.info(url.getFile());
//        Enumeration<URL> urlEnumeration =
//                Thread.currentThread().getContextClassLoader()
//                        .getResources(basePackage.replace(".", "/"));
//        while (urlEnumeration.hasMoreElements()) {
//            url = urlEnumeration.nextElement();
//            logger.info(url);
//        }
        BeanDefinitionReaderUtils.scanPackages(basePackage);


        //List<String> list = loadSummerFactories(classLoader);
        Set<String> list = BeanDefinitionReaderUtils.SCAN_CLASS;
        for(String str : list){
            logger.info(str);
        }
    }
}
