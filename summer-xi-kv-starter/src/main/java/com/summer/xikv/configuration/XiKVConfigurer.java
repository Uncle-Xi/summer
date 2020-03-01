package com.summer.xikv.configuration;

import com.summer.xikv.template.XiKVTemplate;
import com.summerframework.boot.autoconfigure.ConditionalOnProperty;
import com.summerframework.boot.autoconfigure.properties.EnableConfigurationProperties;
import com.summerframework.context.annotation.Bean;
import com.summerframework.context.annotation.Configuration;

/**
 * @description: XiKVConfigurer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Configuration
@ConditionalOnProperty(value = "xi.kv.")
@EnableConfigurationProperties(XiKVProperties.class)
public class XiKVConfigurer {

    @Bean
    public XiKVTemplate xiKVTemplate(XiKVProperties xiKVProperties){
        return new XiKVTemplate(xiKVProperties);
    }
}
