package com.summerframework.boot.web.server.nio;

import com.summerframework.boot.web.server.HttpServlet;
import com.summerframework.core.util.StringUtils;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @description: http netty handler
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpNioHandler {

    private HttpServlet httpServlet;

    public HttpNioHandler(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
    }

    HttpServerRequest request = null;
    HttpServerResponse response = null;

    public void process(SelectionKey selectionKey, Selector selector) throws Exception {
        if (selectionKey.isAcceptable()) {
            acceptAble(selectionKey, selector);
        } else if (selectionKey.isReadable()) {
            readAble(selectionKey);
        } else {
            System.out.println("process else ...");
        }
    }

    private void acceptAble(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void readAble(SelectionKey key) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            channel.read(buffer);
            String request = new String(buffer.array()).trim();
            doHandler(request, channel);
        } catch (Exception e){
            System.out.println("readAble Exception e = " + e);
            e.printStackTrace();
        }
        channel.close();
    }

    private void doHandler(String httpContent, SocketChannel channel) throws Exception{
        if (StringUtils.isEmpty(httpContent)) {
            return;
        }
        request = new HttpServerNioRequest(httpContent);
        response = new HttpServerNioResponse(channel);
        httpServlet.service(request, response);
    }
}
