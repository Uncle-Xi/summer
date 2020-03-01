package com.summerframework.boot.web.server;

import com.summerframework.web.protocol.ServerRequest;
import com.summerframework.web.protocol.ServerResponse;

public interface Servlet {

    void service(ServerRequest request, ServerResponse response) throws Exception;
}
