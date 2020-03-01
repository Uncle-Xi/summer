package com.demo.xipa.plugin;

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

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        Object[] parameter = (Object[]) invocation.getArgs()[1];
//        System.out.println("插件输出：SQL：[" + invocation.getArgs()[0] + "]");
//        System.out.println("插件输出：Parameters："+ Arrays.toString(parameter));
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
