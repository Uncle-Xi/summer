package com.summerframework.web.webmvc.servlet.view;

import com.summerframework.beans.Aware;
import com.summerframework.beans.InitializingBean;
import com.summerframework.context.ConfigurableApplicationContext;
import com.summerframework.core.util.StringUtils;
import com.summerframework.web.webmvc.servlet.View;
import com.summerframework.web.webmvc.servlet.ViewResolver;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: XmlViewResolver
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HtmlViewResolver implements ViewResolver, InitializingBean, Aware {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private final String DEFAULT_VIEW_PREFIX = "html,index";

    public final static String DEFAULT_LOCATION = "view";

    public static final int DEFAULT_CACHE_LIMIT = 1024;

    private final Map<Object, View> viewAccessCache = new ConcurrentHashMap<>(DEFAULT_CACHE_LIMIT);

    private ConfigurableApplicationContext cachedFactory;

    public HtmlViewResolver(){
        init();
    }

    private void init(){
        viewAccessCache.put(DEFAULT_VIEW_PREFIX, new HtmlResourceView(null));
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        String path = (this.getClass().getClassLoader().getResource(DEFAULT_LOCATION).getFile()
                + StringUtils.FOLDER_SEPARATOR + viewName).replaceAll("/+", StringUtils.FOLDER_SEPARATOR);
        System.out.println(path);
        return new HtmlResourceView(new File(path));
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
