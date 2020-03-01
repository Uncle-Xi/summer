package com.summerframework.core;

public interface PropertyResolver {

    String getProperty(String key);

    String getProperty(String key, String defaultValue);

}
