package com.summerframework.web.context.support;

import com.summerframework.context.support.AbstractRefreshableConfigApplicationContext;
import com.summerframework.web.context.ConfigurableWebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableConfigApplicationContext
        implements ConfigurableWebApplicationContext {


    private ServletContext servletContext;


    private ServletConfig servletConfig;


    private String namespace;

    @Override
    public void setServletContext( ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void setServletConfig( ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        if (servletConfig != null && this.servletContext == null) {
            setServletContext(servletConfig.getServletContext());
        }
    }

    @Override

    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public void setNamespace( String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void afterPropertiesSet() throws Exception { }


}
