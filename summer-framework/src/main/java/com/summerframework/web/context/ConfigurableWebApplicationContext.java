package com.summerframework.web.context;

import com.summerframework.context.ConfigurableApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public interface ConfigurableWebApplicationContext extends WebApplicationContext, ConfigurableApplicationContext {

    String getNamespace();

    void setServletContext( ServletContext servletContext);

    void setServletConfig( ServletConfig servletConfig);

    void setNamespace( String namespace);
}
