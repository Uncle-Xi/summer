package com.summerframework.boot.web.webmvc;

import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.boot.web.server.HttpServlet;
import com.summerframework.boot.web.webmvc.view.BootHtmlViewResolver;
import com.summerframework.boot.web.webmvc.view.BootJsonViewResolver;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.logger.PrintColor;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;
import com.summerframework.web.webmvc.servlet.HandlerAdapter;
import com.summerframework.web.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.summerframework.boot.web.server.HttpServer.DEFAULT_PORT;

/**
 * @description: DispatcherServlet
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class BootDispatcherServlet extends HttpServlet {

    private static final LogFactory logger = new LogFactory(BootDispatcherServlet.class);

    public BootDispatcherServlet(ConfigurableApplicationContext context) {
        super(context);
    }

    public void doDispatch(HttpServerRequest request, HttpServerResponse response) throws Exception {
        logger.info(PrintColor.getGreen() + "doDispatch start." + PrintColor.getNone());
        try {
            ModelAndView mv;
            BootHandlerExecutionChain mappedHandler = getHandler(request);
            if (mappedHandler == null) {
                logger.warning("mappedHandler == null");
                noHandlerFound(request, response);
                return;
            }
            BootHandlerAdapter ha = getHandlerAdapter((BootHandlerMapping) mappedHandler.getDefaultHandler());
            if (ha == null) {
                logger.warning("mappedHandler == null");
                noAdapterFound(request, response);
                return;
            }
            mv = ha.handle(request, response, mappedHandler.getDefaultHandler());
            mappedHandler.applyPostHandle(request, response, mv);
            processDispatchResult(request, response, mv);
        } catch (Throwable err) {
            err.printStackTrace();
        } finally {
            logger.info("doDispatch finished...");
        }
    }

    protected void noHandlerFound(HttpServerRequest request, HttpServerResponse response) throws Exception {
        HandlerAdapter.defaultError.getModel().put("error", "no Handler Found...");
        processDispatchResult(request, response, HandlerAdapter.defaultError);
    }

    protected void noAdapterFound(HttpServerRequest request, HttpServerResponse response) throws Exception {
        HandlerAdapter.defaultError.getModel().put("error", "no Adapter Found...");
        processDispatchResult(request, response, HandlerAdapter.defaultError);
    }

    protected BootHandlerExecutionChain getHandler(HttpServerRequest request) throws Exception {
        if (this.handlerMappings != null) {
            for (BootHandlerMapping hm : this.handlerMappings) {
                if (!hm.isMatch(request)) {
                    continue;
                }
                BootHandlerExecutionChain handler = hm.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    private BootHandlerAdapter getHandlerAdapter(BootHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        for (BootHandlerAdapter handlerAdapter : this.handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        return null;
    }

    private void processDispatchResult(HttpServerRequest req, HttpServerResponse resp, ModelAndView mv) throws Exception {
        if (null == mv) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        for (BootViewResolver viewResolver : this.viewResolvers) {
            if (!viewResolver.canResolved(mv.getViewName(), null)) {
                continue;
            }
            BootView view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    @Override
    protected void onRefresh(ApplicationContext context) {
        logger.info("DispatcherServlet onRefresh start...");
        initStrategies(context);
    }

    @Override
    public void doGet(HttpServerRequest request, HttpServerResponse response) throws Exception {
        doDispatch(request, response);
    }

    @Override
    public void doPost(HttpServerRequest request, HttpServerResponse response) throws Exception {
        doGet(request, response);
    }

    protected void initStrategies(ApplicationContext context) {
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initViewResolvers(context);
        logger.info("handlerMappings.size() -> " + handlerMappings.size());
        logger.info("handlerAdapters.size() -> " + handlerAdapters.size());
        logger.info("viewResolvers.size() -> " + viewResolvers.size());
        logger.info("HttpServer started at port[" + DEFAULT_PORT + "]...");
        logger.info("HttpServer init end...");
        openBrowser();
    }

    protected void openBrowser(){
        try {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Runtime.getRuntime().exec("cmd /c start http://localhost:" + DEFAULT_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<BootHandlerMapping> handlerMappings = new ArrayList<>();
    private List<BootHandlerAdapter> handlerAdapters = new ArrayList<>();
    private List<BootViewResolver> viewResolvers = new ArrayList<>();

    private void initHandlerMappings(ApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                //logger.info("initHandlerMappings beanName - " + beanName);
                Object controller = context.getBean(beanName);
                if (controller == null)  { continue; }
                if (!AnnotationConfigUtils.isContentAnnotation(controller, AnnotationConfigUtils.CONTROLLER_ANNOTATIONS)) {
                    continue;
                }
                Method[] methods = controller.getClass().getMethods();
                for (Method method : methods) {
                    if (!AnnotationConfigUtils.isContentAnnotation(method, AnnotationConfigUtils.HANDLER_MAPPING_ANNOTATIONS)){
                        continue;
                    }
                    this.handlerMappings.add(new DefaultHandlerMapping(controller, method));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapters.add(new DefaultHandlerAdapter());
    }

    private void initViewResolvers(ApplicationContext context) {
        this.viewResolvers.add(new BootHtmlViewResolver());
        this.viewResolvers.add(new BootJsonViewResolver());
    }
}
