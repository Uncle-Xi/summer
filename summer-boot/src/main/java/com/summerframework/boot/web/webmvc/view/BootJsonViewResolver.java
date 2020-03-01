package com.summerframework.boot.web.webmvc.view;

import com.summerframework.beans.Aware;
import com.summerframework.beans.InitializingBean;
import com.summerframework.boot.web.webmvc.BootView;
import com.summerframework.boot.web.webmvc.BootViewResolver;

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
public class BootJsonViewResolver implements BootViewResolver, InitializingBean, Aware {

    private final String DEFAULT_TEMPLATE_SUFFIX = "";
    public final static String DEFAULT_LOCATION = "";

    private final String DEFAULT_VIEW_PREFIX = "json,default";
    public static final int DEFAULT_CACHE_LIMIT = 1024;

    private final Map<Object, BootView> viewAccessCache = new ConcurrentHashMap<>(DEFAULT_CACHE_LIMIT);

    public BootJsonViewResolver(){
        init();
    }

    private void init(){
        viewAccessCache.put(DEFAULT_VIEW_PREFIX, new BootJsonResourceView(null));
    }

    @Override
    public BootView resolveViewName(String viewName, Locale locale) throws Exception {
        if (!canResolved(viewName, locale)) {
            return null;
        }
        return new BootJsonResourceView(null);
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
    public void afterPropertiesSet() throws Exception { }

}
