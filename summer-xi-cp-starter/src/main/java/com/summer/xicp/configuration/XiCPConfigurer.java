package com.summer.xicp.configuration;

import com.summer.xicp.template.LockTemplate;
import com.summer.xicp.template.XiCPTemplate;
import com.summerframework.boot.autoconfigure.ConditionalOnProperty;
import com.summerframework.boot.autoconfigure.properties.EnableConfigurationProperties;
import com.summerframework.context.annotation.Bean;
import com.summerframework.context.annotation.Configuration;

/**
 * @description: XiCPConfigurer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Configuration
@ConditionalOnProperty(value = "xi.cp.")
@EnableConfigurationProperties(XiCpProperties.class)
public class XiCPConfigurer {

    @Bean
    public XiCPTemplate xiCPTemplate(XiCpProperties xiCpProperties){
        return new XiCPTemplate(xiCpProperties);
    }

    @Bean
    public LockTemplate lockTemplate(XiCpProperties properties){
        return new LockTemplate(properties);
    }
}
