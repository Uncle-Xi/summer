package com.demo.complete.service.impl;

import com.summer.xicp.template.LockTemplate;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;

/**
 * @description: lock service
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class LockService {

    private static final LogFactory logger = new LogFactory(LockService.class);
    private String LOCK = "lock";
    @Autowired
    LockTemplate lockTemplate;

    public boolean getLock(){
        logger.info("【LockService】【getLock】【OK】");
        return lockTemplate.getLock(LOCK, 5);
    }

    public void releaseLock(){
        logger.info("【LockService】【releaseLock】【OK】");
        lockTemplate.unLock(LOCK);
    }
}
