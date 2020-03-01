package com.summerframework.boot.web.server.socket;

import com.summerframework.boot.web.server.HttpServer;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: HttpSocketServer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpSocketServer extends HttpServer {

    private ServerSocket serverSocket;
    private HttpServerSocketHandler handler;

    public HttpSocketServer(String... port){
        super(port);
    }

    @Override
    public void start() throws RuntimeException {
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            handler = new HttpServerSocketHandler(httpServlet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listening() {
        new Thread(() -> {
            start();
            while (true) {
                try {
                    Socket clientRequest = serverSocket.accept();
                    handler.process(clientRequest);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stop() throws RuntimeException { }
}
