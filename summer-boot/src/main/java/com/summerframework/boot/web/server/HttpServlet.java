package com.summerframework.boot.web.server;

import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ApplicationListener;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.context.event.SourceFilteringListener;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.context.event.ContextRefreshedEvent;
import com.summerframework.web.protocol.ServerRequest;
import com.summerframework.web.protocol.ServerResponse;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;

public abstract class HttpServlet implements Servlet {

    private static final LogFactory logger = new LogFactory(HttpServlet.class);
    
    private ConfigurableApplicationContext context;

    public HttpServlet(){ }

    public HttpServlet(ConfigurableApplicationContext context) {
        this.context = context;
        context.addApplicationListener(new SourceFilteringListener(context, new HttpServlet.ContextRefreshListener()));
        //logger.info("HttpServlet init > 初始化 MVC 事件注册完成.");
    }

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    @Override
    public void service(ServerRequest request, ServerResponse response) throws Exception {
        if (METHOD_GET.equals(request.getProtocol())) {
            doGet((HttpServerRequest)request, (HttpServerResponse)response);
        } else {
            doPost((HttpServerRequest)request, (HttpServerResponse)response);
        }
    }

    private class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            HttpServlet.this.onApplicationEvent(event);
        }
    }

    public void onApplicationEvent(ContextRefreshedEvent event) {
        onRefresh(event.getApplicationContext());
    }

    protected void onRefresh(ApplicationContext context){ }

    public abstract void doGet(HttpServerRequest request, HttpServerResponse response) throws Exception;

    public abstract void doPost(HttpServerRequest request, HttpServerResponse response) throws Exception;
}
