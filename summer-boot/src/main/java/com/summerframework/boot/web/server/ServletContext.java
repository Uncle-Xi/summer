package com.summerframework.boot.web.server;

public interface ServletContext {

    Object getAttribute(String name);

    void setAttribute(String name, Object object);

    void removeAttribute(String name);
}
