package com.summerframework.boot.actuate.mbean;

import com.summerframework.boot.actuate.Status;
import com.summerframework.context.support.AbstractApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @description: Health
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class Health implements HealthMBean {

    private Status status = new Status("UP", "应用程序运行中");
    private Map<String, Object> details = new ConcurrentHashMap<>();
    private Runtime runtime = Runtime.getRuntime();
    AbstractApplicationContext applicationContext;

    public Health(AbstractApplicationContext context){
        this.applicationContext = context;
    }

    @Override
    public Map<String, Object> getJmxMBeanMap() {
        return Collections.unmodifiableMap(fillingData());
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Health getHealth() {
        return this;
    }

    @Override
    public void shutdown() {
        System.exit(0);
    }

    private Map<String, Object> fillingData() {
        //details.clear();
        details.put("freeMemory", runtime.freeMemory());
        details.put("availableProcessors", runtime.availableProcessors());
        details.put("maxMemory", runtime.maxMemory());
        details.put("totalMemory", runtime.totalMemory());
        details.put("beanDefinitionCount", applicationContext.getBeanDefinitionCount());
        StringBuffer bns = new StringBuffer("[");
        AtomicInteger cnt = new AtomicInteger();
        String[] names = applicationContext.getBeanDefinitionNames();
        Stream.of(names).forEach(n -> {
            bns.append("\"" + n + "\"");
            if (cnt.incrementAndGet() < names.length) {
                bns.append(",");
            }
        });
        bns.append("]");
        details.put("beanDefinitionNames", bns);
        return details;
    }

    public static void main(String[] args) {
        String str = ",\n";
        System.out.println(str.length());
    }
}
