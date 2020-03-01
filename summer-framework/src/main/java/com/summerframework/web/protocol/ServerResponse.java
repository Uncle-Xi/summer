package com.summerframework.web.protocol;

import java.io.IOException;
import java.io.OutputStream;

public interface ServerResponse {

    void setContentType(String type);

    String setCharacterEncoding(String charset);

    String getCharacterEncoding();

    OutputStream getOutputStream() throws IOException;

    void printWriter(String print) throws IOException;
}
