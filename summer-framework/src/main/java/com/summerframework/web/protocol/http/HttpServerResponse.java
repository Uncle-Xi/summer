package com.summerframework.web.protocol.http;


import com.summerframework.web.protocol.ServerResponse;

public abstract class HttpServerResponse implements ServerResponse {

    public static final String HTTP_HEAD = "HTTP/1.1 200 OK";
    public static String HTTP_CONTENT_KEY = "Content-Type";
    public static String K_V = ":";
    public static String DEFAULT_HTTP_CONTENT_VAL = "text/html;charset=utf-8";
    public static String CHARSET_UTF8 = "UTF-8";
    public final String DEFAULT_CHARSET = "ISO-8859-1";
    public static final String HTTP_HEAD_DIVIDER = "\r\n";
    public static final String NEW_LINES = "\n";

    @Override
    public String setCharacterEncoding(String charset) {
        return CHARSET_UTF8 = charset;
    }

    @Override
    public String getCharacterEncoding() {
        return CHARSET_UTF8;
    }

}
