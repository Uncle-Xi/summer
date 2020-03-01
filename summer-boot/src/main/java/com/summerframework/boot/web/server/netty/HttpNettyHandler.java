package com.summerframework.boot.web.server.netty;

import com.summerframework.boot.web.server.HttpServlet;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @description: http netty handler
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpNettyHandler extends ChannelInboundHandlerAdapter {

    private HttpServlet httpServlet;

    public HttpNettyHandler(HttpServlet httpServlet){
        //this.serverMapping = serverMapping;
        this.httpServlet = httpServlet;
    }

    HttpServerRequest request = null;
    HttpServerResponse response = null;

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
//        if (msg instanceof HttpRequest){
//            request = new HttpServerNettyRequest(channelHandlerContext, (HttpRequest) msg);
//            response = new HttpServerNettyResponse(channelHandlerContext, (HttpRequest) msg);
//            httpServlet.service(request, response);
//        }

        if (msg instanceof HttpRequest) {
            request = new HttpServerNettyRequest(channelHandlerContext, (HttpRequest) msg);
            response = new HttpServerNettyResponse(channelHandlerContext, (HttpRequest) msg);
            httpServlet.service(request, response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext handlerContext, Throwable cause) throws Exception {
        response = new HttpServerNettyResponse(handlerContext, null);
        response.printWriter("服务器异常请稍后再试！");
        System.out.println("cause -> " + cause);
        cause.printStackTrace();
    }
}
