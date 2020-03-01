package com.summerframework.web.webmvc.servlet;

import java.util.Map;

/**
 * @description: ModelAndView
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ModelAndView<T extends Object> {

    private Object view;

    private Map<String, T> model;

    public ModelAndView(String viewName) {
        this.view = viewName;
    }

    public ModelAndView(String viewName, Map<String, T> model) {
        this.view = viewName;
        this.model = model;
    }

    public String getViewName() {
        return (String) view;
    }

    public Map<String, T> getModel() {
        return model;
    }
}
