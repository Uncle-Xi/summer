package com.demo.complete.plugin;

import com.demo.complete.controller.CacheController;
import com.summerframework.core.logger.LogFactory;
import com.ximq.common.util.StringUtils;
import com.xipa.annotation.Intercepts;
import com.xipa.plugin.Interceptor;
import com.xipa.plugin.Invocation;
import com.xipa.plugin.Plugin;

import java.util.Properties;

/**
 * @description: my plugin
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Intercepts("query")
public class MyPlugin implements Interceptor {

    private static final LogFactory logger = new LogFactory(MyPlugin.class);

    String paramKey;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        logger.info("【MyPlugin】【intercept】-【" + paramKey);
        logger.info("【MyPlugin】【调用方法名】-【" + invocation.getMethod().toString());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        paramKey = properties.getProperty("paramKey");
    }
}
