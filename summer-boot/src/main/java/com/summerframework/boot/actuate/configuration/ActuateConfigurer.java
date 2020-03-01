package com.summerframework.boot.actuate.configuration;

import com.summerframework.boot.autoconfigure.ConditionalOnProperty;
import com.summerframework.boot.autoconfigure.properties.EnableConfigurationProperties;
import com.summerframework.context.annotation.Import;

/**
 * @description: ActuateConfigurer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@ConditionalOnProperty(value = "summer.boot.actuate.")
@EnableConfigurationProperties(ActuateProperties.class)
@Import(ActuateBean.class)
public class ActuateConfigurer {

}
