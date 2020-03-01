package com.demo.complete.controller;

import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RequestParam;
import com.summerframework.web.bind.annotation.RestController;
import com.demo.complete.service.impl.MQService;

/**
 * @description: MQ Controller
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@RestController
public class MQController {

    private static final LogFactory logger = new LogFactory(MQController.class);

    @Autowired
    MQService mqService;

    /**
     * http://localhost:8866/send?topic=hello&data=message
     * @param topic
     * @param date
     * @return
     */
    @GetMapping("/send")
    public String send(@RequestParam(value = "topic") String topic,
                       @RequestParam(value = "data") String date) {
        return "【MQController】【send】【结果】 - 【" + mqService.send(topic, date);
    }
}
