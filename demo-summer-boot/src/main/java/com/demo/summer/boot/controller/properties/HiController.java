package com.demo.summer.boot.controller.properties;

import com.summerframework.boot.web.server.HttpServlet;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: hi controller
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HiController extends HttpServlet {

    @Override
    public void doGet(HttpServerRequest request, HttpServerResponse response) throws Exception {
        Map<String, Object> model = new HashMap();
        model.put("servlet", "Hi Servlet");
        model.put("controller", "Hi Controller");
        StringBuffer responseTxt = new StringBuffer();
        responseTxt.append("[");
        int modelSize = model == null? 0 : 0;
        for (String str : model.keySet()) {
            responseTxt.append("{\"" + str + "\":\"" + model.get(str) + "\"}");
            if (++modelSize < model.size()) {
                responseTxt.append(",");
            }
        }
        responseTxt.append("]");
        response.printWriter(responseTxt.toString());
    }

    @Override
    public void doPost(HttpServerRequest request, HttpServerResponse response) throws Exception {
        doGet(request, response);
    }
}
