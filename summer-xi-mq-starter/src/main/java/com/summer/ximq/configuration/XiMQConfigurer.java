package com.summer.ximq.configuration;

import com.summer.ximq.template.XiMQTemplate;
import com.summerframework.boot.autoconfigure.ConditionalOnProperty;
import com.summerframework.boot.autoconfigure.properties.EnableConfigurationProperties;
import com.summerframework.context.annotation.Bean;
import com.summerframework.context.annotation.Configuration;
import com.summerframework.context.annotation.Import;
import com.summerframework.core.annotation.Order;

/**
 * @description: XiMQConfigurer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Order
@Configuration
@ConditionalOnProperty(value = "xi.mq.")
@EnableConfigurationProperties(XiMQProperties.class)
@Import(XiMQSubscribeBean.class)
public class XiMQConfigurer {

    @Bean
    public XiMQTemplate xiMQTemplate(XiMQProperties properties){
        return new XiMQTemplate(properties);
    }
}
