package com.summerframework.boot.web.server.nio;

import com.summerframework.core.util.StringUtils;
import com.summerframework.web.protocol.http.HttpServerRequest;

/**
 * @description: HttpServerNettyRequest
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpServerNioRequest extends HttpServerRequest {

    private String httpContent;

    public HttpServerNioRequest(String httpContent) {
        this.httpContent = httpContent;
        adapterHttpProtocol();
    }

    private void adapterHttpProtocol(){
        try {
            if (StringUtils.isEmpty(httpContent)){
                return;
            }
            parseHttpContent(httpContent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
