package com.summerframework.boot.web.server.netty;

import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.protocol.http.HttpServerResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @description: HttpServerNettyResponse
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpServerNettyResponse extends HttpServerResponse {

    private static final LogFactory logger = new LogFactory(HttpServerNettyResponse.class);

    public static String HTTP_CONTENT_VAL = DEFAULT_HTTP_CONTENT_VAL;
    private ChannelHandlerContext handlerContext;
    private HttpRequest request;

    public HttpServerNettyResponse(ChannelHandlerContext handlerContext, HttpRequest request) {
        this.request = request;
        this.handlerContext = handlerContext;
    }

    @Override
    public void setContentType(String type) {
        HTTP_CONTENT_VAL = type;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public void printWriter(String print) throws IOException {
        try {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(print.getBytes(CHARSET_UTF8)));
            //response.headers().set("Content-Type", "text/plain;charset=utf-8");
            response.headers().set(HTTP_CONTENT_KEY, HTTP_CONTENT_VAL);
            response.headers().set("vary", "Accept-Encoding");
            if (request != null && HttpUtil.isKeepAlive(request)) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            logger.info("printWriter print -> " + print);
            handlerContext.write(response);
        } finally {
            handlerContext.flush();
            handlerContext.close();
        }
    }
}
