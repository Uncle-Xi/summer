package com.summerframework.boot.web.server.socket;

import com.summerframework.web.protocol.http.HttpServerResponse;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @description: HttpServerNettyResponse
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpServerSocketResponse extends HttpServerResponse {

    public static String HTTP_CONTENT_VAL = DEFAULT_HTTP_CONTENT_VAL;
    private OutputStream outputStream;

    public HttpServerSocketResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public void setContentType(String type) {
        HTTP_CONTENT_VAL = type;
    }

    @Override
    public void printWriter(String print) throws IOException {
        StringBuffer responseContent = new StringBuffer();
        responseContent
                .append(HTTP_HEAD).append(NEW_LINES)
                .append(HTTP_CONTENT_KEY + K_V + HTTP_CONTENT_VAL).append(NEW_LINES)
                .append(HTTP_HEAD_DIVIDER)
                .append(print);
        outputStream.write(responseContent.toString().getBytes(CHARSET_UTF8));
    }
}
