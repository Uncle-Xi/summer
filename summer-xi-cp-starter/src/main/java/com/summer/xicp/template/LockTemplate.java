package com.summer.xicp.template;

import com.summer.xicp.configuration.XiCpProperties;
import com.xicp.WatchedEvent;
import com.xicp.Watcher;
import com.xicp.XiCP;
import com.xicp.client.lock.CompeteLock;

/**
 * @description: lock template
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class LockTemplate {

    XiCpProperties properties;
    CompeteLock competeLock;

    public LockTemplate(XiCpProperties properties) {
        this.properties = properties;
        try {
            this.competeLock = new CompeteLock(properties.getConnectString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getLock(String lock, long timeout){
        return competeLock.lock(lock, timeout);
    }

    public boolean unLock(String lock){
        return competeLock.unLock(lock);
    }
}
