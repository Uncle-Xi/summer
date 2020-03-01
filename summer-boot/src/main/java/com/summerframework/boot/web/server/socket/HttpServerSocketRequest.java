package com.summerframework.boot.web.server.socket;

import com.summerframework.core.util.StringUtils;
import com.summerframework.web.protocol.http.HttpServerRequest;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: HttpServerNettyRequest
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpServerSocketRequest extends HttpServerRequest {

    private InputStream inputStream;

    public HttpServerSocketRequest(InputStream inputStream) {
        this.inputStream = inputStream;
        adapterHttpProtocol();
    }

    private void adapterHttpProtocol() {
        try {
            String httpContent = "";
            byte[] buff = new byte[1024];
            int len = 0;
            if ((len = inputStream.read(buff)) > 0) {
                httpContent = new String(buff, 0, len);
            }
            System.out.printf("httpContent >>> \n[%s]\n", httpContent);
            if (StringUtils.isEmpty(httpContent)) {
                return;
            }
            parseHttpContent(httpContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }
}
