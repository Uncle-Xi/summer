package com.summerframework.web.webmvc.servlet;

import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.context.ApplicationContext;
import com.summerframework.web.webmvc.servlet.mvc.HttpRequestHandlerAdapter;
import com.summerframework.web.webmvc.servlet.server.DefaultWebHandlerMapping;
import com.summerframework.web.webmvc.servlet.view.HtmlViewResolver;
import com.summerframework.web.webmvc.servlet.view.JsonViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description: DispatcherServlet
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DispatcherServlet extends FrameworkServlet {

    private static final Logger logger = Logger.getLogger(DispatcherServlet.class.toString());

    public DispatcherServlet() {
        super();
        // setDispatchOptionsRequest(true);
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            doDispatch(request, response);
        } finally {
            System.err.println("DispatcherServlet doService finally. ");
        }
    }

    public void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            HttpServletRequest processedRequest = request;
            ModelAndView mv;
            HandlerExecutionChain mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(request, response);
                return;
            }
            HandlerAdapter ha = getHandlerAdapter((HandlerMapping) mappedHandler.getDefaultHandler());
            if (ha == null) {
                noAdapterFound(request, response);
                return;
            }
            mv = ha.handle(processedRequest, response, mappedHandler.getDefaultHandler());
            mappedHandler.applyPostHandle(request, response, mv);
            processDispatchResult(processedRequest, response, mv);
        } catch (Throwable err) {
            err.printStackTrace();
        } finally {
            System.out.println("doDispatch finished...");
        }
    }

    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerAdapter.defaultError.getModel().put("error", "no Handler Found...");
        processDispatchResult(request, response, HandlerAdapter.defaultError);
    }

    protected void noAdapterFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerAdapter.defaultError.getModel().put("error", "no Adapter Found...");
        processDispatchResult(request, response, HandlerAdapter.defaultError);
    }


    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            for (HandlerMapping hm : this.handlerMappings) {
                if (!hm.isMatch(request)) {
                    continue;
                }
                HandlerExecutionChain handler = hm.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(HandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        for (HandlerAdapter handlerAdapter : this.handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws Exception {
        if (null == mv) {
            // TODO
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        for (ViewResolver viewResolver : this.viewResolvers) {
            if (!viewResolver.canResolved(mv.getViewName(), null)) {
                continue;
            }
            View view = viewResolver.resolveViewName(mv.getViewName(), null);
            view.render(mv.getModel(), req, resp);
            return;
        }
    }

    @Override
    protected void onRefresh(ApplicationContext context) {
        System.out.printf("DispatcherServlet onRefresh start Context Class = [%s] ....\n", context.getClass().getName());
        initStrategies(context);
    }

    protected void initStrategies(ApplicationContext context) {
        initMultipartResolver(context);
        initLocaleResolver(context);
        initThemeResolver(context);
        initHandlerMappings(context);
        initHandlerAdapters(context);
        initHandlerExceptionResolvers(context);
        initRequestToViewNameTranslator(context);
        initViewResolvers(context);
        initFlashMapManager(context);
    }

    // private boolean detectAllViewResolvers = true;
    private List<ViewResolver> viewResolvers = new ArrayList<>();

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();


    private void initHandlerMappings(ApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);
                if (controller == null) {
                    continue;
                }
                if (!AnnotationConfigUtils.isContentAnnotation(controller, AnnotationConfigUtils.CONTROLLER_ANNOTATIONS)) {
                    continue;
                }
                Method[] methods = controller.getClass().getMethods();
                for (Method method : methods) {
                    if (!AnnotationConfigUtils.isContentAnnotation(method, AnnotationConfigUtils.HANDLER_MAPPING_ANNOTATIONS)){
                        continue;
                    }
                    this.handlerMappings.add(new DefaultWebHandlerMapping(controller, method));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapters.add(new HttpRequestHandlerAdapter());
    }

    private void initViewResolvers(ApplicationContext context) {
        this.viewResolvers.add(new HtmlViewResolver());
        this.viewResolvers.add(new JsonViewResolver());
    }

    private void initThemeResolver(ApplicationContext context) { }

    private void initLocaleResolver(ApplicationContext context) { }

    private void initMultipartResolver(ApplicationContext context) { }

    private void initHandlerExceptionResolvers(ApplicationContext context) { }

    private void initRequestToViewNameTranslator(ApplicationContext context) { }

    private void initFlashMapManager(ApplicationContext context) { }
}
