package com.summerframework.web.webmvc.servlet.view;

import com.summerframework.core.util.StringUtils;
import com.summerframework.web.webmvc.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: HtmlResourceView
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class HtmlResourceView implements View {

    public final String DEFAULT_CONTENT_TYPE = "text/html;charset=utf-8";
    public final String DEFAULT_CHARSET = "ISO-8859-1";
    public final String CHARSET_UTF8 = "utf-8";

    private File viewFile;

    public void setViewFile(File viewFile) {
        this.viewFile = viewFile;
    }

    public HtmlResourceView(File viewFile) {
        this.viewFile = viewFile;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile, "r");
        String line;
        while (null != (line = ra.readLine())) {
            line = new String(line.getBytes(DEFAULT_CHARSET), CHARSET_UTF8);
            Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String paramName = matcher.group();
                paramName = paramName.replaceAll("￥\\{|\\}", StringUtils.NONE_SPACE);
                Object paramValue = model.get(paramName);
                if (null == paramValue) {
                    continue;
                }
                line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                matcher = pattern.matcher(line);
            }
            stringBuffer.append(line);
        }
        System.out.println("发送数据到页面准备就绪【路径】：" + viewFile.getAbsolutePath());
        System.out.println("发送数据到页面准备就绪【数据】：" + stringBuffer);
        response.setCharacterEncoding(CHARSET_UTF8);
        response.setContentType(DEFAULT_CONTENT_TYPE);
        response.getWriter().write(stringBuffer.toString());
    }

    public static String makeStringForRegExp(String str) {
        return str.replace("\\", "\\\\").replace("*", "\\*")
                .replace("+", "\\+").replace("|", "\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }
}
