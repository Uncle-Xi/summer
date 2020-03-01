package com.demo.complete.service.impl;

import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;
import com.demo.complete.dao.UserDao;
import com.demo.complete.domain.User;
import com.demo.complete.service.DemoService;

/**
 * @description: HiDemoServiceImpl
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class DemoServiceImpl implements DemoService {

    private static final LogFactory logger = new LogFactory(DemoServiceImpl.class);

    @Override
    public String hiDemo(String msg) {
        logger.info("HiDemoServiceImpl hiDemo..." + msg);
        return "HiDemoServiceImpl - hiDemo - invoked.";
    }
}
