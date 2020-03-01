package com.summerframework.boot;

public enum WebApplicationType {

    NONE,

    SERVLET,

    REACTIVE;

    static WebApplicationType deduceFromClasspath() {
        return WebApplicationType.SERVLET;
    }

}
