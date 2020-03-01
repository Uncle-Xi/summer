package com.summerframework.boot.web.server.nio;

import com.summerframework.boot.web.server.HttpServer;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: HttpNettyServer
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpNioServer extends HttpServer {

    private Selector selector;
    private HttpNioHandler handler;

    public HttpNioServer(String... port){
        super(port);
    }

    @Override
    public void start() throws Exception {
        selector = Selector.open();
        handler  = new HttpNioHandler(httpServlet);
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(DEFAULT_PORT));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void listening() {
        new Thread(() -> {
            try {
                start();
            } catch (Exception e){
                e.printStackTrace();
            }
            while (true) {
                try {
                    int wait = selector.select();
                    if (wait == 0) {
                        continue;
                    }
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        try {
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            handler.process(key, selector);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stop() throws RuntimeException { }
}
