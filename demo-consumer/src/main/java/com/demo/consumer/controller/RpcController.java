package com.demo.consumer.controller;

import com.demo.consumer.service.RpcService;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Controller;
import com.summerframework.web.bind.annotation.GetMapping;

/**
 * @description: RpcController
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Controller
public class RpcController {

    private static final LogFactory logger = new LogFactory(RpcController.class);

    @Autowired
    RpcService rpcService;

    @GetMapping(value = "/")
    public String index(){
        String result = rpcService.invokeRemoteService();
        logger.info("【消费者调用服务层】【响应】：" + result);
        return "【远程服务消费】【SUCCESS】";
    }
}
