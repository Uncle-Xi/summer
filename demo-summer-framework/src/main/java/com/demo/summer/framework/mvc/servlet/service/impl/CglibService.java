package com.demo.summer.framework.mvc.servlet.service.impl;

import com.summerframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @description: CglibService
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class CglibService {

    private static final Logger logger = Logger.getLogger(CglibService.class.toString());

    //@Autowired
    //HiSummer hiSummer;

    public void cglibHandler(String msg) {
        logger.info("CglibService 处理开始：" + msg);
    }
}
