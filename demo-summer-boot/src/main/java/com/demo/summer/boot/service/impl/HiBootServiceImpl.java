package com.demo.summer.boot.service.impl;

import com.demo.summer.boot.controller.HiSummerBoot;
import com.demo.summer.boot.service.HiBootService;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.beans.factory.annotation.Value;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;

/**
 * @description: HiService implements
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class HiBootServiceImpl implements HiBootService {

    private static final LogFactory logger = new LogFactory(HiBootServiceImpl.class);

    @Value(value = "summer.value.hello")
    private String hello;

    @Autowired
    private HiSummerBoot hiSummer;

    @Override
    public String messageHandler(String msg) {
        logger.info("【HiBootServiceImpl】【开始逻辑处理】【@Value 注解】 - [" + hello);
        hiSummer.sayHi();
        return "【HiBootServiceImpl】【SUCCESS】";
    }

    private void test(){
        logger.info("【循环依赖测试】");
        hiSummer.sayHi();
    }
}
