package com.summerframework.context.support;

import com.summerframework.beans.BeansException;
import com.summerframework.beans.NoSuchBeanDefinitionException;
import com.summerframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ApplicationEvent;
import com.summerframework.context.ApplicationListener;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.io.DefaultResourceLoader;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.Assert;
import com.summerframework.web.context.event.AbstractApplicationEventMulticaster;
import com.summerframework.web.context.event.ApplicationEventMulticaster;
import com.summerframework.web.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    private static final LogFactory logger = new LogFactory(AbstractApplicationContext.class);

    public AbstractApplicationContext(){}
    public AbstractApplicationContext( ApplicationContext parent) {
        setParent(parent);
    }

    private final Object startupShutdownMonitor = new Object();
    private final AtomicBoolean active = new AtomicBoolean();
    private ApplicationContext parent;

    private ApplicationEventMulticaster applicationEventMulticaster;
    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    private Set<ApplicationEvent> earlyApplicationEvents;
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    @Override
    public boolean isActive() {
        return this.active.get();
    }
    private ConfigurableEnvironment environment;

    @Override
    public ConfigurableEnvironment getEnvironment() {
        if (this.environment == null) {
            this.environment = createEnvironment();
        }
        return this.environment;
    }

    protected ConfigurableEnvironment createEnvironment() {
        return new ConfigurableEnvironment();
    }

    @Override

    public ApplicationContext getParent() {
        return this.parent;
    }

    public Collection<ApplicationListener<?>> getApplicationListeners() {
        return this.applicationListeners;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        Assert.notNull(listener, "ApplicationListener must not be null");
        if (this.applicationEventMulticaster != null) {
            logger.info("this.applicationEventMulticaster != null.");
            this.applicationEventMulticaster.addApplicationListener(listener);
        } else {
            logger.info("this.applicationEventMulticaster == null.");
            this.applicationListeners.add(listener);
        }
    }

    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void refresh() throws BeansException, IllegalStateException {
        logger.info("AbstractApplicationContext refresh start...");
        synchronized (this.startupShutdownMonitor) {
            prepareRefresh();
            ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
            prepareBeanFactory(beanFactory);
            try {
                postProcessBeanFactory(beanFactory);
                invokeBeanFactoryPostProcessors(beanFactory);
                // registerBeanPostProcessors(beanFactory);
                // initMessageSource();
                initApplicationEventMulticaster();
                onRefresh();
                registerListeners();
                finishBeanFactoryInitialization(beanFactory);
                finishRefresh();
            } catch (BeansException ex) {
                //destroyBeans();
                //cancelRefresh(ex);
                throw ex;
            } finally {
                //resetCommonCaches();
            }
        }
    }

    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.beforeProcessBeanFactory();
    }

    protected void prepareRefresh(){
        this.earlyApplicationEvents = new LinkedHashSet<>();
    }

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        logger.info("into obtainFreshBeanFactory.");
        refreshBeanFactory();
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        logger.info("Bean factory for : " + beanFactory);
        return beanFactory;
    }

    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        logger.info("DI完成 -> 早期Bean，方法Bean，参数依赖.");
        String[] weaverAwareNames = beanFactory.getBeanDefinitionNames();
        for (String weaverAwareName : weaverAwareNames) {
            getBean(weaverAwareName);
        }
        beanFactory.postProcessEarlyBeanFactory();
    }

    protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;

    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        logger.info("prepareBeanFactory factory for : " + beanFactory);
    }

    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) { }

    protected void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        this.applicationEventMulticaster = new AbstractApplicationEventMulticaster(beanFactory);
        for(ApplicationListener listener : applicationListeners){
            // Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();
            logger.info("initApplicationEventMulticaster applicationEventMulticaster.addApplicationListener(listener).");
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
        if (this.applicationEventMulticaster == null) {
            throw new IllegalStateException("ApplicationEventMulticaster not initialized - " +
                    "call 'refresh' before multicasting events via the context: " + this);
        }
        return this.applicationEventMulticaster;
    }

    protected void registerListeners() {
        for (ApplicationListener<?> listener : getApplicationListeners()) {
            getApplicationEventMulticaster().addApplicationListener(listener);
        }
        Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
        this.earlyApplicationEvents = null;
        if (earlyEventsToProcess != null) {
            for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
                getApplicationEventMulticaster().multicastEvent(earlyEvent);
            }
        }
        logger.info("registerListeners done.");
    }

    public void registerBeanAndDefinition(String beanName, Object object){ }

    @Override
    public void publishEvent(Object event) {
        Assert.notNull(event, "Event must not be null");
        logger.info("Publishing event : " + event);
        if (this.earlyApplicationEvents != null) {
            logger.info("event add to earlyApplicationEvents.");
            this.earlyApplicationEvents.add((ApplicationEvent) event);
        } else {
            logger.info("event multicastEvent getApplicationEventMulticaster.");
            getApplicationEventMulticaster().multicastEvent((ApplicationEvent) event);
        }
    }

    @Override
    public void setParent( ApplicationContext parent) {
        logger.info("setParent parent=[" + parent + "] 方法被调用");
        this.parent = parent;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }


    @Override
    public Object getBean(String name) throws BeansException {
        if (assertBeanFactoryActive(name)) { return null;};
        return getBeanFactory().getBean(name);
    }

    protected boolean assertBeanFactoryActive(String name){
        if (BeanDefinitionReaderUtils.AUTO_SCAN_CLASS_ANNOTATION.equalsIgnoreCase(name)) {
            return true;
        }
        return false;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getType(name);
    }

    protected void onRefresh() throws BeansException {

    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    @Override
    public void close() throws IOException { }

    @Override
    public boolean isRunning() {
        return false;
    }

}
