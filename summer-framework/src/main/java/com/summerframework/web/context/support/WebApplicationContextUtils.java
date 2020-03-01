package com.summerframework.web.context.support;

import com.summerframework.core.util.Assert;
import com.summerframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static com.summerframework.web.webmvc.servlet.FrameworkServlet.DEFAULT_CONTEXT_CLASS;

/**
 * @description: WebApplicationContextUtils
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class WebApplicationContextUtils {

    public static WebApplicationContext getWebApplicationContext(ServletContext sc) {
        return getWebApplicationContext(sc, WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }

    public static WebApplicationContext getWebApplicationContext(ServletContext sc, String attrName) {
        Assert.notNull(sc, "ServletContext must not be null");
        Object attr = sc.getAttribute(attrName);
        System.out.printf("sc.getServletContextName() = [%s]; attrName = [%s]; attr = [%s].\n", sc.getServletContextName(), attrName, attr);
        if (attr == null) {
            return null;
        }
        if (attr instanceof RuntimeException) {
            throw (RuntimeException) attr;
        }
        if (attr instanceof Error) {
            throw (Error) attr;
        }
        if (attr instanceof Exception) {
            throw new IllegalStateException((Exception) attr);
        }
        if (!(attr instanceof WebApplicationContext)) {
            throw new IllegalStateException("Context attribute is not of type WebApplicationContext: " + attr);
        }
        return (WebApplicationContext) attr;
    }

    public static void setWebApplicationContext(ServletContext sc) {
        try {
            sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, DEFAULT_CONTEXT_CLASS.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void LoadProperties(){

    }

}
