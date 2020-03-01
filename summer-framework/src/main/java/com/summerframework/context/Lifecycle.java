package com.summerframework.context;

public interface Lifecycle {
    
    void start();
    
    void stop();
    
    boolean isRunning();
}
