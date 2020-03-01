package com.summerframework.boot.web.webmvc.view;

import com.summerframework.boot.util.TransferFile;
import com.summerframework.boot.web.webmvc.BootView;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.StringUtils;
import com.summerframework.web.protocol.http.HttpServerRequest;
import com.summerframework.web.protocol.http.HttpServerResponse;

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
public class BootHtmlResourceView implements BootView {

    private static final LogFactory logger = new LogFactory(BootHtmlResourceView.class);

    private File viewFile;

    public void setViewFile(File viewFile) {
        this.viewFile = viewFile;
    }

    public BootHtmlResourceView(File viewFile) {
        this.viewFile = viewFile;
    }

    @Override
    public void render(Map<String, ?> model, HttpServerRequest request, HttpServerResponse response) throws Exception {
        StringBuffer respCnt = new StringBuffer();
        File tempFile = TransferFile.getTransferFile(this.viewFile,
                (BootHtmlViewResolver.DEFAULT_LOCATION
                        + StringUtils.FOLDER_SEPARATOR + this.viewFile.getName()));
        RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "r");
        String line;
        while (null != (line = randomAccessFile.readLine())) {
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
            respCnt.append(line);
        }
        randomAccessFile.close();
        TransferFile.deleteFile(tempFile);
        response.setCharacterEncoding(CHARSET_UTF8);
        response.setContentType(HTTP_CONTENT_VAL);
        logger.info("发送数据到页面准备就绪【路径】：" + viewFile.getAbsolutePath());
        logger.info("发送数据到页面准备就绪【数据】：" + respCnt);
        response.printWriter(respCnt.toString());
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
