package com.demo.complete.listener;

import com.summerframework.context.annotation.Component;
import com.summerframework.core.logger.LogFactory;
import com.ximq.common.annotation.XiMQListener;
import com.ximq.common.message.Response;
import com.ximq.common.util.StringUtils;

/**
 * @description: ReceiveMessage
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Component
public class ReceiveMessage {

    private static final LogFactory logger = new LogFactory(ReceiveMessage.class);

    @XiMQListener(topics = "hello", groupId = "demo")
    public void listener(Response response){
        logger.info("【ReceiveMessage】【收到消息】 - 【" + StringUtils.getString(response));
    }
}
