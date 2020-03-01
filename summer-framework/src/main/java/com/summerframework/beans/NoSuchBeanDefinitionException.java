package com.summerframework.beans;

public class NoSuchBeanDefinitionException extends BeansException {


    private String beanName;

    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
        this.beanName = name;
    }

}