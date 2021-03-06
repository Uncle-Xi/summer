package com.demo.summer.boot.starter;

import com.summerframework.boot.web.server.socket.HttpSocketServer;

/**
 * @description: StartSocketServer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class StartSocketServer {

    public static void main(String[] args) throws Exception {
        new HttpSocketServer().listening();
    }
}
