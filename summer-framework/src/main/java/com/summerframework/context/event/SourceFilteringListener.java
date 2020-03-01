package com.summerframework.context.event;

import com.summerframework.context.ApplicationEvent;
import com.summerframework.context.ApplicationListener;
import com.summerframework.core.Ordered;
import com.summerframework.core.ResolvableType;
import com.summerframework.core.logger.LogFactory;

/**
 * @description: SourceFilteringListener
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class SourceFilteringListener implements GenericApplicationListener {

    private static final LogFactory logger = new LogFactory(SourceFilteringListener.class);

    private final Object source;


    private GenericApplicationListener delegate;

    public SourceFilteringListener(Object source, ApplicationListener<?> delegate) {
        this.source = source;
        this.delegate = (delegate instanceof GenericApplicationListener ?
                (GenericApplicationListener) delegate : new GenericApplicationListenerAdapter(delegate));
    }

    protected SourceFilteringListener(Object source) {
        this.source = source;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event.getSource() == this.source) {
            onApplicationEventInternal(event);
        }
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        return (this.delegate == null || this.delegate.supportsEventType(eventType));
    }

    @Override
    public boolean supportsSourceType( Class<?> sourceType) {
        return (sourceType != null && sourceType.isInstance(this.source));
    }

    @Override
    public int getOrder() {
        return (this.delegate != null ? this.delegate.getOrder() : Ordered.LOWEST_PRECEDENCE);
    }

    protected void onApplicationEventInternal(ApplicationEvent event) {
        if (this.delegate == null) {
            throw new IllegalStateException("Must specify a delegate object or override the onApplicationEventInternal method");
        }
        logger.info("onApplicationEventInternal trigger Event = [" + event + "] ....");
        this.delegate.onApplicationEvent(event);
    }
}
