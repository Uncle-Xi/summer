package com.summerframework.web.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public interface HandlerAdapter {

    String view = "default";
    Map<String, Object> nullMap = new HashMap<>();
    Map<String, Object> errorMap = new HashMap<>();
    Map<String, Object> exceptionMap = new HashMap<>();
    ModelAndView defaultNull = new ModelAndView(view, nullMap);
    ModelAndView defaultError = new ModelAndView(view, errorMap);
    ModelAndView defaultException = new ModelAndView(view, exceptionMap);

    boolean supports(Object handler);

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
