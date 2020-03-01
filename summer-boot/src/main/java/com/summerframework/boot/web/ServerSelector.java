package com.summerframework.boot.web;

import com.summerframework.boot.web.server.HttpServer;
import com.summerframework.boot.web.server.netty.HttpNettyServer;
import com.summerframework.boot.web.server.nio.HttpNioServer;
import com.summerframework.boot.web.server.socket.HttpSocketServer;

/**
 * @description: ServerSelector
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class ServerSelector {

    private static final String NIO = "NIO";
    private static final String SOCKET = "SOCKET";
    private static final String NETTY = "NETTY";

    public static HttpServer getServer(String serverType, String ... port){
        if (NIO.equalsIgnoreCase(serverType)) {
            return new HttpNioServer(port);
        } else if (SOCKET.equalsIgnoreCase(serverType)) {
            return new HttpSocketServer(port);
        } else {
            return new HttpNettyServer(port);
        }
    }
}
