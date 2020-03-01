package com.summer.ximq.template;

import com.summer.ximq.XiMQConfig;
import com.summer.ximq.configuration.XiMQProperties;
import com.summer.ximq.configuration.XiMQSubscribeBean;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.core.logger.LogFactory;
import com.ximq.clients.RecordFuture;
import com.ximq.clients.producer.Producer;
import com.ximq.common.util.StringUtils;

/**
 * @description: XiMQTemplate
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class XiMQTemplate extends XiMQConfig {

    private static final LogFactory logger = new LogFactory(XiMQTemplate.class);

    private Producer producer;

    XiMQProperties xp;

    public XiMQTemplate(XiMQProperties xp){
        this.xp = xp;
    }

    public void send(String topic, Object data, RecordFuture future){
        synchronized (this){
            if (producer == null) {
                logger.info("[XiMQTemplate] [创建Producer]");
                this.producer = new Producer(getProperty(xp));
            }
        }
        //System.out.println("[topic] - [" + topic);
        //System.out.println("[data] - [" + data);
        //System.out.println("[xp] - [" + StringUtils.getString(xp));
        producer.send(topic, data, future);
    }
}
