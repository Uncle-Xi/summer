package com.summerframework.boot.actuate.configuration;

import com.summerframework.beans.factory.annotation.Value;
import com.summerframework.boot.autoconfigure.properties.ConfigurationProperties;

/**
 * @description: XiMQProperties
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@ConfigurationProperties
public class ActuateProperties {

    @Value(value = "summer.boot.actuate.enable")
    public String enable;

    @Value(value = "summer.boot.actuate.password")
    public String password;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{ \"enable\" : \"" + enable + "\", \"password\" : \"" + password + "\"}";
    }
}
