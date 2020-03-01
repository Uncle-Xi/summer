package com.summerframework.web.context.event;

import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ApplicationEvent;

/**
 * @description: ApplicationContextEvent
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(ApplicationContext source) {
        super(source);
    }

    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
