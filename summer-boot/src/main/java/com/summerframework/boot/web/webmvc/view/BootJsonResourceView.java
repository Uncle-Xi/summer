package com.summerframework.boot.web.webmvc.view;

import com.summerframework.boot.web.webmvc.BootView;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;

import java.io.File;
import java.util.Map;

/**
 * @description: JsonResourceView
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class BootJsonResourceView implements BootView {

    private static final LogFactory logger = new LogFactory(BootJsonResourceView.class);

    private File viewFile;

    public void setViewFile(File viewFile) {
        this.viewFile = viewFile;
    }

    public BootJsonResourceView(File viewFile) {
        this.viewFile = viewFile;
    }

    @Override
    public void render(Map<String, ?> model, HttpServerRequest request, HttpServerResponse response) throws Exception {
        StringBuffer respCnt = new StringBuffer();
        respCnt.append("[");
        int modelSize = 0;
        for (String str : model.keySet()) {
            // respCnt.append("{\"key\":\"" + str + "\",\"value\":\"" + model.get(str) + "\"}");
            respCnt.append("{");
            boolean obj = str.contains("[") ? true : str.contains("{") ? true : false;
            respCnt.append(obj == true ? str : "\"" + str + "\"");
            respCnt.append(":");
            String val = "" + model.get(str);
            obj = val.contains("[") ? true : val.contains("{") ? true : false;
            respCnt.append(obj == true ? val : "\"" + val + "\"");
            respCnt.append("}");
            if (++modelSize < model.size()) {
                respCnt.append(",");
            }
        }
        respCnt.append("]");
        response.setCharacterEncoding(CHARSET_UTF8);
        response.setContentType(JSON_CONTENT_VAL);
        response.printWriter(respCnt.toString());
        //response.printWriter(URLEncoder.encode(respCnt.toString(), "UTF-8"));
    }
}
