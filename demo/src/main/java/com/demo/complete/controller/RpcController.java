package com.demo.complete.controller;

import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.bind.annotation.GetMapping;
import com.summerframework.web.bind.annotation.RestController;
import com.demo.complete.rpc.RpcService;

/**
 * @description: RpcController
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@RestController
public class RpcController {

    private static final LogFactory logger = new LogFactory(RpcController.class);

    @Autowired
    RpcService rpcService;

    /**
     * http://localhost:8866/rpc
     * @return
     */
    @GetMapping("/rpc")
    public String rpc() {
        return "【RpcController】【rpc】【结果】 - 【" + rpcService.rpcSev();
    }
}
