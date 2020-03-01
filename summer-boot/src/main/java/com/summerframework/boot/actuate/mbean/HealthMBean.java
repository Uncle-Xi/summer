package com.summerframework.boot.actuate.mbean;

import com.summerframework.boot.actuate.Status;
import com.summerframework.boot.actuate.mbean.Health;

import java.util.Map;

public interface HealthMBean {

    Map<String, Object> getJmxMBeanMap();

    Status getStatus();

    Health getHealth();

    void shutdown();
}
