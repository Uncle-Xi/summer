package com.summerframework.web.webmvc.servlet;

import com.summerframework.beans.BeanUtils;
import com.summerframework.beans.BeansException;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ApplicationContextAware;
import com.summerframework.context.ApplicationListener;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.context.event.SourceFilteringListener;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.web.context.ConfigurableWebApplicationContext;
import com.summerframework.web.context.WebApplicationContext;
import com.summerframework.web.context.event.ContextRefreshedEvent;
import com.summerframework.web.context.support.WebApplicationContextUtils;
import com.summerframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @description: FrameworkServlet
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {

    private static final Logger logger = Logger.getLogger(FrameworkServlet.class.toString());

    public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";

    private WebApplicationContext webApplicationContext;
    private boolean webApplicationContextInjected = false;
    private String namespace;
    private boolean refreshEventReceived = false;
    public static final Class<?> DEFAULT_CONTEXT_CLASS = XmlWebApplicationContext.class;
    private Class<?> contextClass = DEFAULT_CONTEXT_CLASS;

    public Class<?> getContextClass() {
        return this.contextClass;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        namespace = (this.namespace != null ? this.namespace : getServletName() + DEFAULT_NAMESPACE_SUFFIX);
        return namespace;
    }

    public final WebApplicationContext getWebApplicationContext() {
        return this.webApplicationContext;
    }

    @Override
    protected final void initServletBean() throws ServletException {
        logger.info("FrameworkServlet '" + getServletName() + "': initialization started");
        long startTime = System.currentTimeMillis();
        try {
            this.webApplicationContext = initWebApplicationContext();
            initFrameworkServlet();
        } catch (Exception ex) {
            logger.warning("Context initialization failed" + ex);
            throw ex;
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("FrameworkServlet '" + getServletName() + "': initialization completed in " + elapsedTime + " ms");
    }

    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        try {
            doService(request, response);
        } catch (Exception e) {
            response.getWriter().println(" " + e.getMessage());
            e.printStackTrace();
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            logger.info("请求处理用时：" + processingTime + ".ms");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (this.webApplicationContext == null && applicationContext instanceof WebApplicationContext) {
            this.webApplicationContext = (WebApplicationContext) applicationContext;
            this.webApplicationContextInjected = true;
        }
    }

    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext rootContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        //if (rootContext == null) {
        //    System.out.println("initWebApplicationContext 得到默认值");
        //    rootContext = getDefaultWebApplicationContext();
        //}
        WebApplicationContext wac = null;
        if (this.webApplicationContext != null) {
            wac = this.webApplicationContext;
            if (wac instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
                if (!cwac.isActive()) {
                    if (cwac.getParent() == null) {
                        cwac.setParent(rootContext);
                    }
                    System.out.println("this.webApplicationContext != null.");
                    configureAndRefreshWebApplicationContext(cwac);
                }
            }
        }
        if (wac == null) {
            wac = createWebApplicationContext(rootContext);
        }
        if (!this.refreshEventReceived) {
            System.out.println("refreshEventReceived == false.");
            onRefresh(wac);
        }
        return wac;
    }

    private WebApplicationContext getDefaultWebApplicationContext() {
        try {
            return (WebApplicationContext) DEFAULT_CONTEXT_CLASS.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected WebApplicationContext createWebApplicationContext( ApplicationContext parent) {
        Class<?> contextClass = getContextClass();
        ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        wac.setParent(parent == null ? wac : parent);
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }

    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
        wac.setServletContext(getServletContext());
        wac.setServletConfig(getServletConfig());
        wac.setEnvironment((ConfigurableEnvironment) getEnvironment());
        wac.setNamespace(getNamespace());
        wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));
        postProcessWebApplicationContext(wac);
        applyInitializers(wac);
        wac.refresh();
    }

    /**
     * IOC 容器初始化完成事件通知
     */
    private class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            FrameworkServlet.this.onApplicationEvent(event);
        }
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.refreshEventReceived = true;
        onRefresh(event.getApplicationContext());
    }

    protected void onRefresh(ApplicationContext context) {
    }

    protected abstract void doService(HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected void applyInitializers(ConfigurableApplicationContext wac) {
        // TODO 属性设置
    }

    protected void initFrameworkServlet() throws ServletException {
        // TODO
    }

    protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {
        // TODO.
    }
}
