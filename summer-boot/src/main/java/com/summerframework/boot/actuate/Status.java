package com.summerframework.boot.actuate;

import com.summerframework.core.util.Assert;

/**
 * @description: Status
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class Status {

    public static final Status UNKNOWN = new Status("UNKNOWN");
    public static final Status UP = new Status("UP");
    public static final Status DOWN = new Status("DOWN");
    public static final Status OUT_OF_SERVICE = new Status("OUT_OF_SERVICE");
    private final String code;
    private final String description;

    public Status(String code) {
        this(code, "");
    }

    public Status(String code, String description) {
        Assert.notNull(code, "Code must not be null");
        Assert.notNull(description, "Description must not be null");
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "{\"code\": \"" + code + "\",\"description\": \"" + description + "\"}";
    }
}
