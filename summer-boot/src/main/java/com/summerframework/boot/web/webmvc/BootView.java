package com.summerframework.boot.web.webmvc;

import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;

import java.util.Map;

public interface BootView {

    String HTTP_CONTENT_VAL = "text/html;charset=utf-8";
    String JSON_CONTENT_VAL = "text/json;charset=utf-8";
    String DEFAULT_CHARSET = "ISO-8859-1";
    String CHARSET_UTF8 = "utf-8";

    void render(Map<String, ?> model, HttpServerRequest request, HttpServerResponse response) throws Exception;
}
