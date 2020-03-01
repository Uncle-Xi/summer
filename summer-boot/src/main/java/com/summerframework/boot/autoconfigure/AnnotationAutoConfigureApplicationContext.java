package com.summerframework.boot.autoconfigure;

import com.summerframework.aop.autoproxy.DefaultAdvisorAutoProxyCreator;
import com.summerframework.beans.BeansException;
import com.summerframework.beans.NoSuchBeanDefinitionException;
import com.summerframework.beans.factory.config.BeanDefinition;
import com.summerframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.summerframework.beans.factory.support.BeanDefinitionRegistry;
import com.summerframework.boot.autoconfigure.util.AutoConfigureBeanDefinitionReaderUtils;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.context.DefaultListableBeanFactory;
import com.summerframework.context.annotation.AnnotatedBeanDefinitionReader;
import com.summerframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.summerframework.context.support.AbstractApplicationContext;
import com.summerframework.context.support.ApplicationContextAwareProcessor;
import com.summerframework.core.logger.LogFactory;

/**
 * @description: AnnotationAutoConfigureApplicationContext
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AnnotationAutoConfigureApplicationContext extends AbstractApplicationContext
        implements ConfigurableApplicationContext, BeanDefinitionRegistry {

    private static final LogFactory logger = new LogFactory(AnnotationAutoConfigureApplicationContext.class);
    
    private AnnotatedBeanDefinitionReader reader;

    private ClassPathBeanDefinitionScanner scanner;

    private DefaultListableBeanFactory beanFactory;

    public AnnotationAutoConfigureApplicationContext(){
        this.reader = new AnnotatedBeanDefinitionReader(this, getEnvironment());
        this.scanner = new ClassPathBeanDefinitionScanner(this, getEnvironment());
    }

    public AnnotationAutoConfigureApplicationContext(ApplicationContext applicationContext) {
        super(applicationContext);
        this.reader = new AnnotatedBeanDefinitionReader(this, getEnvironment());
        this.scanner = new ClassPathBeanDefinitionScanner(this, getEnvironment());
        this.beanFactory = new DefaultListableBeanFactory();
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        //super.postProcessBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.addBeanPostProcessor(new DefaultAdvisorAutoProxyCreator(beanFactory, getEnvironment()));
        //logger.info("AnnotationAutoConfigureApplicationContext postProcessBeanFactory -> ");
        //logger.info("this.getEnvironment() -> " + this.getEnvironment());
    }

    @Override
    protected void refreshBeanFactory() throws BeansException, IllegalStateException {
        //logger.info("本地包扫描 -> " + AutoConfigureBeanDefinitionReaderUtils.LOCAL_CLASS.size());
        AutoConfigureBeanDefinitionReaderUtils.registerLocalBeanDefinition(
                beanFactory, AutoConfigureBeanDefinitionReaderUtils.LOCAL_CLASS);
        //logger.info("扩展包扫描 -> " + AutoConfigureBeanDefinitionReaderUtils.FACTORIES_CLASS.size());
        AutoConfigureBeanDefinitionReaderUtils.registerFactoriesBeanDefinition(
                beanFactory, AutoConfigureBeanDefinitionReaderUtils.FACTORIES_CLASS);
    }

    @Override
    public void registerBeanAndDefinition(String beanName, Object object){
        beanFactory.registerBeanAndDefinition(beanName, object);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws RuntimeException {
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return this.beanFactory.getBeanDefinition(beanName);
    }

    @Override
    public void registerEarlyBeanDefinition(String beanName, BeanDefinition beanDefinition) throws RuntimeException {
        this.beanFactory.registerEarlyBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        if (beanFactory == null) {
            this.beanFactory = new DefaultListableBeanFactory();
        }
        beanFactory.setEnvironment(getEnvironment());
        return beanFactory;
    }
}
