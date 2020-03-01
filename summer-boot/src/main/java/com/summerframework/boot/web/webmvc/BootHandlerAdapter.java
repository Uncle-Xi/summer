package com.summerframework.boot.web.webmvc;

import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;
import com.summerframework.web.webmvc.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public interface BootHandlerAdapter {

    String view = "default";
    Map<String, Object> nullMap = new HashMap<>();
    Map<String, Object> errorMap = new HashMap<>();
    Map<String, Object> exceptionMap = new HashMap<>();
    ModelAndView defaultNull = new ModelAndView(view, nullMap);
    ModelAndView defaultError = new ModelAndView(view, errorMap);
    ModelAndView defaultException = new ModelAndView(view, exceptionMap);

    boolean supports(Object handler);

    ModelAndView handle(HttpServerRequest request, HttpServerResponse response, Object handler) throws Exception;
}
