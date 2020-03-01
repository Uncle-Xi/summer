package com.demo.summer.framework.mvc.servlet.service.impl;

import com.demo.summer.framework.mvc.servlet.controller.HiSummer;
import com.demo.summer.framework.mvc.servlet.service.HiService;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.beans.factory.annotation.Value;
import com.summerframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @description: HiService implements
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class HiServiceImpl implements HiService {

    private static final Logger logger = Logger.getLogger(HiServiceImpl.class.toString());

    @Value(value = "summer.value.hello")
    private String hello;

    @Autowired
    private HiSummer hiSummer;

    @Override
    public String messageHandler(String msg) {
        logger.info("HiServiceImpl 处理开始：");
        System.out.println("HiServiceImpl 使用 @Value 注解：" + hello);
        hiSummer.sayHi();
        return "处理开始： < " + msg + " < 处理结束...";
    }
}
