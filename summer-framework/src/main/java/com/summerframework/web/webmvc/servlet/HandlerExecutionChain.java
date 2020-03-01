package com.summerframework.web.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: HandlerExecutionChain
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HandlerExecutionChain {

    private Object defaultHandler;
    private HandlerInterceptor[] interceptors;
    private List<HandlerInterceptor> interceptorList;
    private int interceptorIndex = -1;
    public Object getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(Object defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    private List<HandlerInterceptor> initInterceptorList() {
        if (this.interceptorList == null) {
            this.interceptorList = new ArrayList<>();
            if (this.interceptors != null) {
            }
        }
        this.interceptors = null;
        return this.interceptorList;
    }


    public HandlerInterceptor[] getInterceptors() {
        if (this.interceptors == null && this.interceptorList != null) {
            this.interceptors = this.interceptorList.toArray(new HandlerInterceptor[0]);
        }
        return this.interceptors;
    }

    boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return true;
    }

    void applyPostHandle(HttpServletRequest request, HttpServletResponse response,  ModelAndView mv) throws Exception {
        // TODO
    }

}
