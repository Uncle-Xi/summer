package com.summerframework.boot.web.webmvc;

import com.summerframework.web.protocol.http.HttpServerRequest;

import java.lang.reflect.Method;

public interface BootHandlerMapping {

    Method getHandlerMapping();

    Object getController();

    boolean isMatch(HttpServerRequest request);

    BootHandlerExecutionChain getHandler(HttpServerRequest request) throws Exception;
}
