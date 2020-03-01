package com.summerframework.boot.web.server;

public interface Lifecycle {

    String START_EVENT = "start";

    String STOP_EVENT = "stop";

    void init() throws RuntimeException;

    void start() throws RuntimeException, Exception;

    void stop() throws RuntimeException;
}
