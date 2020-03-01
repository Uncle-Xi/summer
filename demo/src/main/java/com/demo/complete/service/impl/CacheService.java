package com.demo.complete.service.impl;

import com.summer.xikv.template.XiKVTemplate;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;

/**
 * @description: CacheService
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class CacheService {

    private static final LogFactory logger = new LogFactory(CacheService.class);

    @Autowired
    XiKVTemplate xiKVTemplate;

    public void setCache(String key, String val, long timeout) throws InterruptedException {
        logger.info("【CacheService】【setCache】【OK】" + xiKVTemplate.set(key, val, timeout));
    }

    public String getCache(String key) throws InterruptedException {
        logger.info("【CacheService】【getCache】【OK】");
        return xiKVTemplate.get(key);
    }
}
