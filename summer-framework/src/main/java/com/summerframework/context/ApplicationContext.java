package com.summerframework.context;

import com.summerframework.core.env.EnvironmentCapable;

public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, ApplicationEventPublisher {

    ApplicationContext getParent();
}
