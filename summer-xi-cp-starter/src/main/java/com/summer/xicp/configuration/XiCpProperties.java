package com.summer.xicp.configuration;

import com.summerframework.beans.factory.annotation.Value;
import com.summerframework.boot.autoconfigure.properties.ConfigurationProperties;

/**
 * @description: XiCpProperties
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@ConfigurationProperties
public class XiCpProperties {

    @Value(value = "xi.cp.connect.address")
    private String connectString;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }
}
