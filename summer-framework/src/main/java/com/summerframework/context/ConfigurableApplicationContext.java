package com.summerframework.context;

import com.summerframework.beans.BeansException;
import com.summerframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.summerframework.core.env.ConfigurableEnvironment;

import java.io.Closeable;

public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {

    String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

    void refresh() throws BeansException, IllegalStateException;

    boolean isActive();

    void setParent( ApplicationContext parent);

    void addApplicationListener(ApplicationListener<?> listener);

    void setEnvironment(ConfigurableEnvironment environment);

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}
