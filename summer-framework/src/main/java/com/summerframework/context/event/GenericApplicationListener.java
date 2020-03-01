package com.summerframework.context.event;

import com.summerframework.context.ApplicationEvent;
import com.summerframework.context.ApplicationListener;
import com.summerframework.core.Ordered;
import com.summerframework.core.ResolvableType;

public interface GenericApplicationListener extends ApplicationListener <ApplicationEvent>, Ordered {

    boolean supportsEventType(ResolvableType eventType);

    boolean supportsSourceType( Class<?> sourceType);

}