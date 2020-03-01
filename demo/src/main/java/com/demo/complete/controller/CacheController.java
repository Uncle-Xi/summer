package com.demo.complete.controller;

import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RequestParam;
import com.summerframework.web.bind.annotation.RestController;
import com.demo.complete.service.impl.CacheService;

/**
 * @description: CacheController
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@RestController
public class CacheController {

    private static final LogFactory logger = new LogFactory(CacheController.class);

    @Autowired
    CacheService cacheService;

    /**
     * http://localhost:8866/setCache?key=unclexi&val=HelloUncleXi
     * @param key
     * @param val
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/setCache")
    public String setCache(@RequestParam(value = "key") String key,
                           @RequestParam(value = "val") String val) throws InterruptedException {
        cacheService.setCache(key, val, 50);
        return "【CacheController】【setCache】【SUCCESS】";
    }

    /**
     * http://localhost:8866/getCache?key=unclexi
     * @param key
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/getCache")
    public String getCache(@RequestParam(value = "key") String key) throws InterruptedException {
        return "【CacheController】【getCache】【结果】 - 【"
                + cacheService.getCache(key);
    }
}
