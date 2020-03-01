package com.summer.xikv.template;

import com.summer.xikv.configuration.XiKVProperties;
import com.xikv.XiKV;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: XiKVTemplate
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XiKVTemplate {

    XiKVProperties properties;
    XiKV xiKV;

    public XiKVTemplate(XiKVProperties properties){
        this.properties = properties;
        this.xiKV = new XiKV(properties.getConnectString(), properties.getPassword());
    }

    public boolean set(String key, String val, long timeout) throws InterruptedException {
        return this.xiKV.set(key, val, timeout);
    }

    public String get(String key) throws InterruptedException {
        return this.xiKV.get(key);
    }

    public boolean del(String key) throws InterruptedException {
        return this.xiKV.del(key);
    }

    public int incr(String key, String val, long timeout) throws InterruptedException {
        return this.xiKV.incr(key, val, timeout);
    }

    public int decr(String key, String val, long timeout) throws InterruptedException {
        return this.xiKV.decr(key, val, timeout);
    }

    public boolean setNx(String key, String val, long timeout) throws InterruptedException {
        return this.xiKV.setNx(key, val, timeout);
    }

    public boolean exists(String key) throws InterruptedException {
        return this.xiKV.exists(key);
    }

    public List lGet(String key) throws InterruptedException {
        return this.xiKV.lGet(key);
    }

    public void lSet(String key, List val, long timeout) throws InterruptedException {
        this.xiKV.lSet(key, val, timeout);
    }

    public Map hGet(String key) throws InterruptedException {
        return this.xiKV.hGet(key);
    }

    public void hSet(String key, Map val, long timeout) throws InterruptedException {
        this.xiKV.hSet(key, val, timeout);
    }

    public Set sGet(String key) throws InterruptedException {
        return this.xiKV.sGet(key);
    }

    public void sSet(String key, Set val, long timeout) throws InterruptedException {
        this.xiKV.sSet(key, val, timeout);
    }

    public int size(String key) throws InterruptedException {
        return this.xiKV.size(key);
    }
}
