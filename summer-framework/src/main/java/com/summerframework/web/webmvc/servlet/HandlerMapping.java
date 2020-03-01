package com.summerframework.web.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public interface HandlerMapping {

    Method getHandlerMapping();

    Object getController();

    boolean isMatch(HttpServletRequest request);


    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
