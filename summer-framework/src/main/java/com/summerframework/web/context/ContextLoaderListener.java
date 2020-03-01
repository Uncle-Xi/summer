package com.summerframework.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @description: ContextLoaderListener
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ContextLoaderListener implements ServletContextListener {

    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ContextLoaderListener ServletContextEvent contextInitialized... ");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ContextLoaderListener ServletContextEvent contextDestroyed... ");
    }
}
