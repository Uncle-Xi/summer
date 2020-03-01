package com.summerframework.context.event;

import com.summerframework.context.ApplicationEvent;
import com.summerframework.context.ApplicationListener;
import com.summerframework.core.ResolvableType;
import com.summerframework.core.util.Assert;

public class GenericApplicationListenerAdapter implements GenericApplicationListener{

    private final ApplicationListener<ApplicationEvent> delegate;


    private final ResolvableType declaredEventType;

    public GenericApplicationListenerAdapter(ApplicationListener<?> delegate) {
        Assert.notNull(delegate, "Delegate listener must not be null");
        this.delegate = (ApplicationListener<ApplicationEvent>) delegate;
        this.declaredEventType = resolveDeclaredEventType(this.delegate);
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        return false;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return false;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        this.delegate.onApplicationEvent(event);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private static ResolvableType resolveDeclaredEventType(ApplicationListener<ApplicationEvent> listener) {

        return null;
    }
}
