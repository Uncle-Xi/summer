package com.summerframework.beans;

/**
 * @description: PropertyValue
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class PropertyValue {

    private final String name;


    private final Object value;

    public PropertyValue(String name,  Object newValue) {
        this.name = name;
        this.value = newValue;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
