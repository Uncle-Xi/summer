package com.summerframework.web.webmvc.servlet.server;

import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.core.util.StringUtils;
import com.summerframework.web.webmvc.servlet.HandlerExecutionChain;
import com.summerframework.web.webmvc.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
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
public class DefaultWebHandlerMapping implements HandlerMapping {

    private Object controller;
    private Method handlerMapping;
    private Pattern pattern;
    private String regex;
    private final Map<String, Object> urlMap = new LinkedHashMap<>();
    private final Map<String, Object> handlerMap = new LinkedHashMap<>();

    public DefaultWebHandlerMapping(Object controller, Method handlerMapping) {
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
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        HandlerExecutionChain handlerExecutionChain = new HandlerExecutionChain();
        handlerExecutionChain.setDefaultHandler(this);
        return handlerExecutionChain;
    }

    @Override
    public Object getController() {
        return controller;
    }

    @Override
    public boolean isMatch(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String url = request.getRequestURI();
        url = url.replace(contextPath, StringUtils.NONE_SPACE).replaceAll("/+", StringUtils.FOLDER_SEPARATOR);
        Matcher matcher = this.pattern.matcher(url);
        System.out.printf(">>>>>> RequestURI = [%s]; handlerMapping = [%s]\n", url, this.regex);
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
