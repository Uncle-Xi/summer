package com.demo.complete.service.impl;

import com.summer.ximq.template.XiMQTemplate;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;
import com.ximq.common.util.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description: MQ Service
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class MQService {

    private static final LogFactory logger = new LogFactory(MQService.class);
    private static ExecutorService executor = Executors.newFixedThreadPool(8);

    @Autowired
    XiMQTemplate xiMQTemplate;

    public boolean send(String topic, String data){
        if (topic == null || data == null || "".equalsIgnoreCase(topic) || "".equalsIgnoreCase(data)) {
            throw new RuntimeException("【topic 与 data 不允许为空】");
        }
        executor.submit(() -> {
            logger.info(Thread.currentThread().getId() + "】 - 【MQService】【send】【SUCCESS】");
            xiMQTemplate.send(topic, data, response -> {
                logger.info("【MQService】【send】【response】 - 【" + StringUtils.getString(response));
            });
        });
        return true;
    }
}
