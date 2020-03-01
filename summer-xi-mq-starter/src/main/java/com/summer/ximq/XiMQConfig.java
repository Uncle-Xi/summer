package com.summer.ximq;

import com.summer.ximq.configuration.XiMQProperties;
import com.summer.ximq.configuration.XiMQSubscribe;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.beans.factory.annotation.Value;
import com.ximq.common.config.ClientConfig;

import java.util.Properties;

/**
 * @description: XiMQStarter
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XiMQConfig {

    protected Properties getProperty(XiMQProperties xp){
        Properties properties = new Properties();
        properties.setProperty(ClientConfig.XIMQ_SERVER_CONNECT_STRING, xp.getConnectString());
        properties.setProperty(ClientConfig.XIMQ_SEND_MESSAGE_ACK_MODEL, xp.getAckModel());
        properties.setProperty(ClientConfig.XIMQ_GROUP_ID, xp.getGroupId());
        return properties;
    }

    protected XiMQSubscribe xiMQSubscribe = new XiMQSubscribe();
}
