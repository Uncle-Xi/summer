package com.summer.ximq.configuration;

import com.ximq.clients.consumer.Consumer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Properties;

/**
 * @description: XiMQSubscribe
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XiMQSubscribe {

    public void doSubscribe(Properties properties, Collection<String> topics, Object object, Method method){
        Consumer consumer = new Consumer(properties);
        consumer.subscribe(topics, response -> {
            try {
                method.invoke(object, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
