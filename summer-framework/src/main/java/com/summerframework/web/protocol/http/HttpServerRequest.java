package com.summerframework.web.protocol.http;

import com.summerframework.web.protocol.ServerRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpServerRequest implements ServerRequest {

    protected String requestMethod;
    protected String requestUri;
    protected String contextPath;
    protected String protocol;
    protected String charset;
    protected Map<String, Object> paramMap = new HashMap<>();
    protected Map<String, Object> attributeMap = new HashMap<>();

    protected void parseHttpContent(String httpContent) {
        System.out.println("HttpServerNioRequest adapterHttpProtocol -> \n" + httpContent);
        String line = httpContent.split("\\n")[0];
        System.out.println("HTTP HEAD -> " + line);
        String[] arr = line.split("\\s");
        String method = arr[0];
        String url = arr[1];
        String[] urlArr = url.contains("?") ? arr[1].split("\\?") : new String[]{url, ""};
        this.requestMethod = method;
        this.requestUri = urlArr[0];
        this.contextPath = urlArr[1];
        System.out.println(requestUri);
    }

    @Override
    public String getContextPath() { return contextPath; }

    @Override
    public String getRequestURI() {
        return requestUri;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getCharacterEncoding() {
        return charset;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap;
    }

    @Override
    public String getParameter(String name) {
        return (String) paramMap.get(name);
    }

    @Override
    public Map getParameterMap() {
        return paramMap;
    }

    @Override
    public String getMethod() {
        return requestMethod;
    }
}