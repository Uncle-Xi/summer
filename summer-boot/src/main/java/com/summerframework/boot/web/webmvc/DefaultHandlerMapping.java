package com.summerframework.boot.web.webmvc;

import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.StringUtils;
import com.summerframework.web.protocol.http.HttpServerRequest;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: default web HandlerMapping
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DefaultHandlerMapping implements BootHandlerMapping {

    private static final LogFactory logger = new LogFactory(DefaultHandlerMapping.class);

    private Object controller;
    private Method handlerMapping;
    private Pattern pattern;
    private String regex;
    private final Map<String, Object> urlMap = new LinkedHashMap<>();
    private final Map<String, Object> handlerMap = new LinkedHashMap<>();

    public DefaultHandlerMapping(Object controller, Method handlerMapping) {
        this.controller = controller;
        this.handlerMapping = handlerMapping;
        initializingMapping();
    }

    public void initializingMapping() {
        String baseUrl = AnnotationConfigUtils.getAnnotationValue(controller, AnnotationConfigUtils.CONTROLLER_ANNOTATIONS);
        String requestMappingValue = AnnotationConfigUtils.getAnnotationValue(handlerMapping, AnnotationConfigUtils.HANDLER_MAPPING_ANNOTATIONS);
        this.regex = (StringUtils.FOLDER_SEPARATOR + baseUrl + StringUtils.FOLDER_SEPARATOR
                + requestMappingValue.replaceAll("\\*", ".*"))
                .replaceAll("/+", StringUtils.FOLDER_SEPARATOR);
        this.pattern = Pattern.compile(this.regex);
    }

    @Override
    public BootHandlerExecutionChain getHandler(HttpServerRequest request) throws Exception {
        BootHandlerExecutionChain handlerExecutionChain = new BootHandlerExecutionChain();
        handlerExecutionChain.setDefaultHandler(this);
        return handlerExecutionChain;
    }

    @Override
    public Object getController() {
        return controller;
    }

    @Override
    public boolean isMatch(HttpServerRequest request) {
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        uri = uri.replace(contextPath, StringUtils.NONE_SPACE).replaceAll("/+", StringUtils.FOLDER_SEPARATOR);
        Matcher matcher = this.pattern.matcher(uri);
        //logger.info(">>>>>> RequestURI = [" + uri + "]; ContextPath = [" + contextPath + "]");
        logger.info("[<!>] handlerMapping = [" + this.regex + "]; matcher.matches() = [" + matcher.matches() + "]");
        return matcher.matches();
    }

    @Override
    public Method getHandlerMapping() {
        return handlerMapping;
    }

    public void setMappings(String key, Object value) {
        urlMap.put(key, value);
    }

    public void setUrlMap(Map<String, ?> urlMap) {
        this.urlMap.putAll(urlMap);
    }

    public Map<String, ?> getUrlMap() {
        return this.urlMap;
    }
}
