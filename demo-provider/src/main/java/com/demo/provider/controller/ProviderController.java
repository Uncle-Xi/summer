package com.demo.provider.controller;

import com.demo.api.hi.HiRpc;
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
public class ProviderController {

    private static final LogFactory logger = new LogFactory(ProviderController.class);

    @Autowired
    HiRpc hiRpc;

    @GetMapping(value = "/")
    public String index(){
        String result = (String) hiRpc.helloRpc();
        logger.info("【提供者调用服务层】【响应】：" + result);
        return "【测试提供者是否可用】【SUCCESS】";
    }
}
