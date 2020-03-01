package com.summerframework.web.context.event;

import com.summerframework.context.ApplicationEvent;
import com.summerframework.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);
}
