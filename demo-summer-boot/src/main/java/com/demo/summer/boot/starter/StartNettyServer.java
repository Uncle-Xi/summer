package com.demo.summer.boot.starter;


import com.summerframework.boot.web.server.netty.HttpNettyServer;

/**
 * @description: StartNettyServer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class StartNettyServer {

    public static void main(String[] args) {
        new HttpNettyServer().listening();
    }
}
