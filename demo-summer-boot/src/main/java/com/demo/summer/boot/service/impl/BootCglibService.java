package com.demo.summer.boot.service.impl;

import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;

/**
 * @description: CglibService
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class BootCglibService {

    private static final LogFactory logger = new LogFactory(BootCglibService.class);

    //@Autowired
    //HiSummer hiSummer;

    public void cglibHandler(String msg) {
        logger.info("【AOP】【Cglib动态代理】【服务调用成功】" + msg);
    }
}
