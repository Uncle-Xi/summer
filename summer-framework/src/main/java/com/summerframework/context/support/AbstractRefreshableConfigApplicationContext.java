package com.summerframework.context.support;

import com.summerframework.aop.autoproxy.DefaultAdvisorAutoProxyCreator;
import com.summerframework.beans.Aware;
import com.summerframework.beans.BeansException;
import com.summerframework.beans.InitializingBean;
import com.summerframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.DefaultListableBeanFactory;

import java.io.IOException;

public abstract class AbstractRefreshableConfigApplicationContext extends AbstractApplicationContext
        implements Aware, InitializingBean {

    private final Object beanFactoryMonitor = new Object();
    private DefaultListableBeanFactory beanFactory;

    private String[] configLocations;

    public void setConfigLocation(String location) {
        setConfigLocations();
    }

    public void setConfigLocations( String... locations) {
        if (locations != null && locations.length > 0) {
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                this.configLocations[i] = locations[i].trim();
            }
        } else {
            this.configLocations = new String[]{"application.properties"};
        }
    }


    private void setDefaultProperties() {
        try {
            if (this.getEnvironment().getDefaultProperties() != null) {
                return;
            }
            if (configLocations == null || configLocations.length == 0) {
                setConfigLocations();
            }
            this.getEnvironment().initEnvironment(configLocations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AbstractRefreshableConfigApplicationContext() {
    }

    public AbstractRefreshableConfigApplicationContext( ApplicationContext parent) {
        super(parent);
    }

    @Override
    public final ConfigurableListableBeanFactory getBeanFactory() {
        synchronized (this.beanFactoryMonitor) {
            if (this.beanFactory == null) {
                throw new IllegalStateException("BeanFactory not initialized or already closed - " +
                        "call 'refresh' before accessing beans via the ApplicationContext");
            }
            return this.beanFactory;
        }
    }

    @Override
    protected final void refreshBeanFactory() throws BeansException {
        try {
            setDefaultProperties();
            DefaultListableBeanFactory beanFactory = createBeanFactory();
            customizeBeanFactory(beanFactory);
            loadBeanDefinitions(beanFactory);
            synchronized (this.beanFactoryMonitor) {
                this.beanFactory = beanFactory;
            }
        } catch (Exception ex) {
            throw new RuntimeException("I/O error parsing bean definition source for ", ex);
        }
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        //super.postProcessBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        beanFactory.addBeanPostProcessor(new DefaultAdvisorAutoProxyCreator(beanFactory, getEnvironment()));
    }

    protected String[] getConfigLocations() {
        return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
    }

//    @Override
//    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
//        super.postProcessBeanFactory(beanFactory);
//        beanFactory.addBeanPostProcessor(new DefaultAdvisorAutoProxyCreator());
//    }


    protected String[] getDefaultConfigLocations() {
        return null;
    }

    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        beanFactory.setEnvironment(getEnvironment());
    }

    protected DefaultListableBeanFactory createBeanFactory() {
        if (beanFactory != null) {
            return beanFactory;
        }
        return new DefaultListableBeanFactory(getParent());
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
            throws BeansException, IOException;
}
