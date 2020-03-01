package com.summerframework.web.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface ServerRequest {

    String getProtocol();

    String getRequestURI();

    String getMethod();

    String getCharacterEncoding();

    InputStream getInputStream() throws IOException;

    Object getAttribute(String name);

    String getParameter(String name);

    String getContextPath();

    Map getParameterMap();

}
