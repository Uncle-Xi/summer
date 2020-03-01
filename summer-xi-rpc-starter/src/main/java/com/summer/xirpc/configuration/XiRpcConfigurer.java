package com.summer.xirpc.configuration;

import com.summerframework.boot.autoconfigure.ConditionalOnProperty;
import com.summerframework.context.annotation.Import;

/**
 * @description: XiRpcConfigurer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@ConditionalOnProperty(value = "xi.rpc.")
@Import(XiRpcImportSelector.class)
public class XiRpcConfigurer  {

}
