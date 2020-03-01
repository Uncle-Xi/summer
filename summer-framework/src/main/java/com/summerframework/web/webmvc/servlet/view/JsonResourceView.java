package com.summerframework.web.webmvc.servlet.view;

import com.summerframework.web.webmvc.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * @description: JsonResourceView
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class JsonResourceView implements View {

    public final String DEFAULT_CONTENT_TYPE = "text/json;charset=utf-8";
    public final String DEFAULT_CHARSET = "ISO-8859-1";
    public final String CHARSET_UTF8 = "utf-8";

    private File viewFile;

    public void setViewFile(File viewFile) {
        this.viewFile = viewFile;
    }

    public JsonResourceView(File viewFile) {
        this.viewFile = viewFile;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        int modelSize = model == null? 0 : 0;
        for (String str : model.keySet()) {
            stringBuffer.append("{\"key\":\"" + str + "\",\"value\":\"" + model.get(str) + "\"}");
            if (++modelSize < model.size()) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("]");
        response.setCharacterEncoding(DEFAULT_CHARSET);
        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.getWriter().write(stringBuffer.toString());
    }
}
