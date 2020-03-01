package com.summerframework.boot.web.webmvc;

import java.util.Locale;

public interface BootViewResolver {

    boolean canResolved(String viewName, Locale locale);

    BootView resolveViewName(String viewName, Locale locale) throws Exception;
}
