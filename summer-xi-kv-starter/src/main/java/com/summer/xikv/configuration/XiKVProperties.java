package com.summer.xikv.configuration;

import com.summerframework.beans.factory.annotation.Value;
import com.summerframework.boot.autoconfigure.properties.ConfigurationProperties;
import com.xikv.XiKV;

/**
 * @description: XiKVProperties
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@ConfigurationProperties
public class XiKVProperties {

    @Value(value = "xi.kv.connect.address")
    private String connectString;

    @Value(value = "xi.kv.connect.password")
    private String password;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
