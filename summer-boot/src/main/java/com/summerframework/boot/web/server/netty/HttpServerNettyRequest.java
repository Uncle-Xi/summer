package com.summerframework.boot.web.server.netty;

import com.summerframework.core.util.StringUtils;
import com.summerframework.web.protocol.http.HttpServerRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

import java.util.*;

/**
 * @description: HttpServerNettyRequest
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpServerNettyRequest extends HttpServerRequest {

    private ChannelHandlerContext channelHandlerContext;
    private HttpRequest httpRequest;
    //private FullHttpRequest httpRequest;

    public HttpServerNettyRequest(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) {
        this.channelHandlerContext = channelHandlerContext;
        this.httpRequest = httpRequest;
    }

    @Override
    public String getContextPath() {
        String uri = httpRequest.uri();
        return uri.contains("?") ? "?" + uri.split("\\?")[1] : StringUtils.NONE_SPACE;
    }

    @Override
    public String getRequestURI() {
        return httpRequest.uri();
    }

    @Override
    public String getMethod() {
        return httpRequest.method().name();
    }

    @Override
    public Map getParameterMap() {
        String method = getMethod();
        Map<String, List<String>> parameters = new HashMap<>();
        if ("GET".equals(method)) {
            QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
            parameters = decoder.parameters();
        } else {
            HttpPostRequestDecoder decoder =
                    new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), httpRequest);
            List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
            for (InterfaceHttpData data : postData) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    MemoryAttribute attribute = (MemoryAttribute) data;
                    parameters.put(attribute.getName(), Collections.singletonList(attribute.getValue()));
                }
            }
        }
        return parameters;
    }

    @Override
    public String getParameter(String name) {
        Map<String, List<String>> params = getParameterMap();
        List<String> param = params.get(name);
        if (null == param) {
            System.out.println("getParameter null == param ");
            return null;
        } else {
            return param.get(0);
        }
    }
}
