package com.summerframework.web.webmvc.servlet.view;

import com.summerframework.beans.Aware;
import com.summerframework.beans.InitializingBean;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.web.webmvc.servlet.View;
import com.summerframework.web.webmvc.servlet.ViewResolver;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: JsonViewResolver
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class JsonViewResolver implements ViewResolver, InitializingBean, Aware {

    private final String DEFAULT_TEMPLATE_SUFFIX = "";

    private final String DEFAULT_VIEW_PREFIX = "json,default";

    public final static String DEFAULT_LOCATION = "";

    public static final int DEFAULT_CACHE_LIMIT = 1024;

    private final Map<Object, View> viewAccessCache = new ConcurrentHashMap<>(DEFAULT_CACHE_LIMIT);

    private ConfigurableApplicationContext cachedFactory;

    public JsonViewResolver(){
        init();
    }

    private void init(){
        viewAccessCache.put(DEFAULT_VIEW_PREFIX, new JsonResourceView(null));
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        if (!canResolved(viewName, locale)) {
            return null;
        }
        return new JsonResourceView(null);
    }

    @Override
    public boolean canResolved(String viewName, Locale locale){
        if (null == viewName || "".equals(viewName.trim())) {
            return false;
        }
        if (DEFAULT_VIEW_PREFIX.contains(viewName)) {
            return true;
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO
    }

}
