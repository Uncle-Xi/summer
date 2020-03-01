package com.summerframework.boot.web.server;

import com.summerframework.boot.web.webmvc.BootDispatcherServlet;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.core.logger.LogFactory;

/**
 * @description: HttpServer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class HttpServer extends GenericServer {

    private static final LogFactory logger = new LogFactory(HttpServer.class);

    public static final String DEFAULT_PROPERTIES_NAME = "application.properties";

    public static int DEFAULT_PORT = 8080;

    //protected Map<String, HttpServlet> serverMapping = new ConcurrentHashMap<>();

    protected HttpServlet httpServlet;

    //public Properties properties = new Properties();

    protected HttpServer(String ... port){
        if (port != null && port.length > 0 && port[0] != null) {
            DEFAULT_PORT = Integer.valueOf(port[0]);
        }
    }

    @Override
    public void init() throws RuntimeException {
        try {
            //context.addApplicationListener(new SourceFilteringListener(context, null));
            //System.out.println("HttpSocketServer init start...");
            //String defaultPropertiesPath;
            //URL propertiesUrl = this.getClass().getResource(DEFAULT_PROPERTIES_NAME);
            //if (propertiesUrl == null) {
            //    propertiesUrl = this.getClass().getResource("/");
            //    defaultPropertiesPath = propertiesUrl.getPath() + DEFAULT_PROPERTIES_NAME;
            //} else {
            //    defaultPropertiesPath = propertiesUrl.getPath();
            //}
            //if (defaultPropertiesPath == null) {
            //    throw new RuntimeException("资源配置不存在！");
            //}
            //properties.load(new FileInputStream(defaultPropertiesPath));
            //for (Object k : properties.keySet()) {
            //    String key = k.toString();
            //    if (key.endsWith(".uri")) {
            //        String servletName = key.replaceAll("\\.uri$", "");
            //        String url = properties.getProperty(key);
            //        String className = properties.getProperty(servletName + ".className");
            //        HttpServlet httpServer = (HttpServlet) Class.forName(className).newInstance();
            //        serverMapping.put(url, httpServer);
            //    }
            //    System.out.printf("HttpNettyServer serverMapping add -> [%s]...\n", key);
            //}
            //System.out.println("HttpNettyServer init end...");

            httpServlet = new BootDispatcherServlet(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRefresh(ConfigurableApplicationContext context) {
        init();
    }
}
