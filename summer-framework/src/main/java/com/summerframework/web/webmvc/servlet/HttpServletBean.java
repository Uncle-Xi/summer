package com.summerframework.web.webmvc.servlet;

import com.summerframework.context.EnvironmentAware;
import com.summerframework.core.env.Environment;
import com.summerframework.core.env.EnvironmentCapable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.logging.Logger;

public abstract class HttpServletBean extends HttpServlet implements EnvironmentCapable, EnvironmentAware {

    private static final Logger logger = Logger.getLogger(FrameworkServlet.class.toString());

    @Override
    public final void init() throws ServletException {
        logger.info("Initializing servlet '" + getServletName() + "'");
        initServletBean();
        logger.info("Servlet '" + getServletName() + "' configured successfully");
    }

    protected void initServletBean() throws ServletException { }

    @Override
    public String getServletName() {
        return (getServletConfig() != null ? getServletConfig().getServletName() : null);
    }

    @Override
    public void setEnvironment(Environment environment) {}

    @Override
    public Environment getEnvironment() {
        return null;
    }
}
