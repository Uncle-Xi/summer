package com.demo.summer.boot.starter;

import com.summerframework.boot.web.server.nio.HttpNioServer;

/**
 * @description: StartNioServer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class StartNioServer {

    public static void main(String[] args) throws Exception {
        new HttpNioServer().listening();
    }
}
