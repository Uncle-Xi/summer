package com.summerframework.web.context.event;

import com.summerframework.context.ApplicationContext;

/**
 * @description: ContextRefreshedEvent
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

}
