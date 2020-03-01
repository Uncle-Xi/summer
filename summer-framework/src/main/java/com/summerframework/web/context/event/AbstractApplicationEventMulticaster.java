package com.summerframework.web.context.event;

import com.summerframework.beans.BeanFactory;
import com.summerframework.beans.BeansException;
import com.summerframework.beans.NoSuchBeanDefinitionException;
import com.summerframework.beans.factory.BeanFactoryAware;
import com.summerframework.context.ApplicationEvent;
import com.summerframework.context.ApplicationListener;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @description: AbstractApplicationEventMulticaster
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);
    private BeanFactory beanFactory;
    private Object retrievalMutex = this.defaultRetriever;

    public AbstractApplicationEventMulticaster(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }


    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        synchronized (this.retrievalMutex) {
            this.defaultRetriever.applicationListeners.add(listener);
        }
    }

    private BeanFactory getBeanFactory() {
        if (this.beanFactory == null) {
            throw new IllegalStateException("ApplicationEventMulticaster cannot retrieve listener beans " +
                    "because it is not associated with a BeanFactory");
        }
        return this.beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    protected Collection<ApplicationListener<?>> getApplicationListeners() {
        synchronized (this.retrievalMutex) {
            return this.defaultRetriever.getApplicationListeners();
        }
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener listener : getApplicationListeners()) {
            listener.onApplicationEvent(event);
        }
    }

    private class ListenerRetriever {

        public final Set<ApplicationListener<?>> applicationListeners;
        public final Set<String> applicationListenerBeans;
        private final boolean preFiltered;

        public ListenerRetriever(boolean preFiltered) {
            this.applicationListeners = new LinkedHashSet<>();
            this.applicationListenerBeans = new LinkedHashSet<>();
            this.preFiltered = preFiltered;
        }

        public Collection<ApplicationListener<?>> getApplicationListeners() {
            LinkedList<ApplicationListener<?>> allListeners = new LinkedList<>();
            for (ApplicationListener<?> listener : this.applicationListeners) {
                allListeners.add(listener);
            }
            if (!this.applicationListenerBeans.isEmpty()) {
                BeanFactory beanFactory = getBeanFactory();
                for (String listenerBeanName : this.applicationListenerBeans) {
                    try {
                        ApplicationListener<?> listener = beanFactory.getBean(listenerBeanName, ApplicationListener.class);
                        if (this.preFiltered || !allListeners.contains(listener)) {
                            allListeners.add(listener);
                        }
                    }
                    catch (NoSuchBeanDefinitionException ex) {
                        // Singleton listener instance (without backing bean definition) disappeared -
                        // probably in the middle of the destruction phase
                    }
                }
            }
            //AnnotationAwareOrderComparator.sort(allListeners);
            return allListeners;
        }
    }
}
