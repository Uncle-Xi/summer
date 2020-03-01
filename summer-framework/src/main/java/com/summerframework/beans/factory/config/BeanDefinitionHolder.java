package com.summerframework.beans.factory.config;

/**
 * @description: BeanDefinitionHolder
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class BeanDefinitionHolder {

    private final BeanDefinition beanDefinition;

    private final String beanName;

    private String[] aliases;

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName,  String[] aliases)  {
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
        this.aliases = aliases;
    }

    public BeanDefinition getBeanDefinition() {
        return this.beanDefinition;
    }

    public String getBeanName() {
        return this.beanName;
    }


    public String[] getAliases() {
        return this.aliases;
    }
}
