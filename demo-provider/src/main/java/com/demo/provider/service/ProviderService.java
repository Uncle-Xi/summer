package com.demo.provider.service;

import com.demo.api.hi.HiRpc;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.stereotype.Service;
import com.xirpc.config.annotation.Provider;

/**
 * @description: RpcService
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Provider
@Service
public class ProviderService implements HiRpc {

    private static final LogFactory logger = new LogFactory(ProviderService.class);

    @Override
    public Object helloRpc() {
        logger.info("【ProviderService】【正常提供服务】");
        return "SUCCESS";
    }
}
