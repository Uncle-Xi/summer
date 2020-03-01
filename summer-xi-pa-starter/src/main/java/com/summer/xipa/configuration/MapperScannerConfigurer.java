package com.summer.xipa.configuration;

import com.summer.xipa.mapper.MapperSupport;
import com.summerframework.beans.BeansException;
import com.summerframework.beans.FactoryBean;
import com.summerframework.beans.InitializingBean;
import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.boot.autoconfigure.ConditionalOnProperty;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ApplicationContextAware;
import com.summerframework.context.support.AbstractApplicationContext;
import com.summerframework.core.annotation.Order;
import com.summerframework.core.config.BeanPostProcessor;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.ClassUtils;
import com.summerframework.core.util.StringUtils;
import com.xipa.io.Resources;
import com.xipa.session.SqlSessionFactory;
import com.xipa.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: SqlSessionFactoryBuilderConfiguration
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Order
@ConditionalOnProperty(value = "summer.boot.xipa.")
public class MapperScannerConfigurer implements ApplicationContextAware, BeanPostProcessor,
        InitializingBean, FactoryBean {

    private static final LogFactory logger = new LogFactory(MapperScannerConfigurer.class);

    private final static String XIPA_CONFIG_LOCATION = "xipa-config.xml";
    private final static String XIPA_CONFIG_PREFIX = "summer.boot.xipa.config";
    private final static String XIPA_DAO_INTERFACES_PREFIX = "summer.boot.xipa.dao";
    private static boolean isJar = false;

    private AbstractApplicationContext applicationContext;
    private ConfigurableEnvironment environment;
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AbstractApplicationContext) applicationContext;
        this.environment = this.applicationContext.getEnvironment();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("MapperScannerConfigurer afterPropertiesSet initXIPA ");
        initXiPA();
    }

    protected void initXiPA() throws Exception {
        String resource = environment.getProperty(XIPA_CONFIG_PREFIX, XIPA_CONFIG_LOCATION);
        logger.info(resource);
        Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
                .getResources(resource);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equalsIgnoreCase(protocol)) {
                isJar = true;
                doLoadJar(url, resource);
            } else {
                doLoadLocal(resource);
            }
            break;
        }
        scanDaoInterface();
    }

    protected void doLoadLocal(String resource) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    protected void doLoadJar(URL url, String resource) throws Exception {
        //File tempFile = TransferFile.getTransferFile(new File(resource), resource);
        //InputStream inputStream = new FileInputStream(tempFile);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(url.openStream());
        //if (inputStream != null) { inputStream.close(); }
        //TransferFile.deleteFile(tempFile);
    }

    protected void scanDaoInterface() throws Exception {
        String daoPackage = environment.getProperty(XIPA_DAO_INTERFACES_PREFIX);
        if (daoPackage == null) {
            return;
        }
        Set<String> classNameList = new HashSet<>();
        if (isJar) {
            doScanJarDao(daoPackage, classNameList);
        } else {
            doScanLocalDao(daoPackage, classNameList);
        }
        registerBeanToFactory(classNameList);
    }

    protected static void doScanJarDao (String basePackage, Set<String> classList) throws IOException {
        Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
                .getResources(basePackage.replaceAll("\\.", StringUtils.FOLDER_SEPARATOR));
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            BeanDefinitionReaderUtils.doScanJar(basePackage, url, classList);
        }
    }

    protected void doScanLocalDao(String daoPackage, Set<String> classNameList){
        URL url = ClassUtils.getDefaultClassLoader().getResource(
                daoPackage.replaceAll("\\.", StringUtils.FOLDER_SEPARATOR));
        if (url == null) { return; }
        File classPath = new File(url.getFile());
        if (classPath == null) { return; }
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanLocalDao(daoPackage + StringUtils.CURRENT_PATH + file.getName(), classNameList);
            } else {
                if (!file.getName().endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                    continue;
                }
                String className = getClassName(daoPackage, file.getName());
                //System.out.println("doScanLocalDao -> " + className);
                classNameList.add(className);
            }
        }
    }

    private String getClassName(String basePackage, String fileName){
        return basePackage + StringUtils.CURRENT_PATH + fileName
                .replace(ClassUtils.CLASS_FILE_SUFFIX, StringUtils.NONE_SPACE);
    }

    protected void registerBeanToFactory(Set<String> classNameList) throws Exception {
        for (String className : classNameList) {
            Class<?> clazz = Class.forName(className);
            Object object = new SqlSessionInterceptor(
                    new MapperSupport(sqlSessionFactory, clazz)).getProxy();
            if (object == null) {
                return;
            }
            //sqlSessionFactory.getConfiguration().addMapper(clazz);
            applicationContext.registerBeanAndDefinition(className, object);
        }
    }

    private class SqlSessionInterceptor implements InvocationHandler {

        private MapperSupport mapperSupport;

        public SqlSessionInterceptor(MapperSupport mapperSupport){
            this.mapperSupport = mapperSupport;
        }

        public Object getProxy() {
            return Proxy.newProxyInstance(mapperSupport.getMapperInterface().getClassLoader(),
                    new Class[]{this.mapperSupport.getMapperInterface()}, this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                Object result = method.invoke(mapperSupport.getMapper(), args);
                return result;
            } catch (Throwable ta) {
                ta.printStackTrace();
            }
            return null;
        }
    }
}
