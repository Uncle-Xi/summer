package com.summerframework.boot.web.server.socket;

import com.summerframework.boot.web.server.HttpServlet;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @description: HttpServerSocketHandler
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HttpServerSocketHandler {

    private HttpServlet httpServlet;
    private HttpServerRequest request = null;
    private HttpServerResponse response = null;

    public HttpServerSocketHandler(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
    }

    public void process(Socket client) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = client.getInputStream();
            outputStream = client.getOutputStream();
            request = new HttpServerSocketRequest(inputStream);
            response = new HttpServerSocketResponse(outputStream);
            httpServlet.service(request, response);
        } catch (Exception e) {
            if (client != null && !client.isClosed()) {
                client.getOutputStream().write(500);
            }
        } finally {
            if (inputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (client != null && !client.isClosed()) {
                client.close();
            }
        }
    }
}
