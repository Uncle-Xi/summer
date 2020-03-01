package com.demo.consumer.service;

import com.demo.api.hi.HiRpc;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;
import com.xirpc.config.annotation.Reference;

/**
 * @description: RpcService
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Service
public class RpcService {

    private static final LogFactory logger = new LogFactory(RpcService.class);

    @Reference
    HiRpc hiRpc;

    public String invokeRemoteService(){
        Object result = hiRpc.helloRpc();
        logger.info("【消费者调用远程服务】【收到结果】：" + result);
        return "SUCCESS";
    }
}
