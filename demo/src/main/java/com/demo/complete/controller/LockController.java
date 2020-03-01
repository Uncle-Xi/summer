package com.demo.complete.controller;

import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RestController;
import com.demo.complete.service.impl.LockService;

/**
 * @description: lock controller
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@RestController
public class LockController {

    @Autowired
    LockService lockService;

    @GetMapping("/lock")
    public String getLock(){
        return "【LockController】【getLock】【结果】 - 【" + lockService.getLock();
    }

    @GetMapping("/unLock")
    public String unLock(){
        lockService.releaseLock();
        return "【LockController】【unLock】【SUCCESS】";
    }
}
