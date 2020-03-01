package com.summerframework.web.webmvc.servlet;

import java.util.Locale;

public interface ViewResolver {

    boolean canResolved(String viewName, Locale locale);

    View resolveViewName(String viewName, Locale locale) throws Exception;
}
