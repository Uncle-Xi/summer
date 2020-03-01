package com.demo.summer.boot.service;

import com.demo.summer.boot.service.impl.BootCglibService;
import com.demo.summer.boot.service.impl.HiBootServiceImpl;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;

/**
 * @description: ConstructorService
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class ConstructorService {

    private static final LogFactory logger = new LogFactory(HiBootServiceImpl.class);

    BootCglibService bootCglibService;

    public ConstructorService(BootCglibService bootCglibService){
        this.bootCglibService = bootCglibService;
    }

    public void constructorInvoke(){
        logger.info("【构造器注入】【调用成功】");
        bootCglibService.cglibHandler("【开始调用】【构造器注入】【CglibService】");
    }
}
