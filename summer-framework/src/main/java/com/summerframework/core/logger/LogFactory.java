package com.summerframework.core.logger;

import com.summerframework.core.util.StringUtils;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description: LogFactory
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class LogFactory {


    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy年-MM月-dd日 HH:mm:ss:SSS");

    private static final String SPAN = " ";

    private Class<?> clazz;

    public LogFactory(Class<?> clazz){
        this.clazz = clazz == null? this.getClass() : clazz;
    }

    /**
     * Map<String, String> map = System.getenv();
     *
     * java.version Java ：运行时环境版本
     * java.vendor Java ：运行时环境供应商
     * java.vendor.url ：Java供应商的 URL
     * java.home &nbsp;&nbsp;：Java安装目录
     * java.vm.specification.version： Java虚拟机规范版本
     * java.vm.specification.vendor ：Java虚拟机规范供应商
     * java.vm.specification.name &nbsp; ：Java虚拟机规范名称
     * java.vm.version ：Java虚拟机实现版本
     * java.vm.vendor ：Java虚拟机实现供应商
     * java.vm.name&nbsp; ：Java虚拟机实现名称
     * java.specification.version：Java运行时环境规范版本
     * java.specification.vendor：Java运行时环境规范供应商
     * java.specification.name ：Java运行时环境规范名称
     * java.class.version ：Java类格式版本号
     * java.class.path ：Java类路径
     * java.library.path  ：加载库时搜索的路径列表
     * java.io.tmpdir  ：默认的临时文件路径
     * java.compiler  ：要使用的 JIT编译器的名称
     * java.ext.dirs ：一个或多个扩展目录的路径
     * os.name ：操作系统的名称
     * os.arch  ：操作系统的架构
     * os.version  ：操作系统的版本
     * file.separator ：文件分隔符
     * path.separator ：路径分隔符
     * line.separator ：行分隔符
     * user.name ：用户的账户名称
     * user.home ：用户的主目录
     * user.dir：用户的当前工作目录
     *
     * @param log
     */
    public void info(Object log){
        System.out.println(
                PrintColor.getGreen() + LogLevel.INFO + PrintColor.getNone() + SPAN + getDate() + SPAN
                        + PrintColor.getViolet() + Thread.currentThread().getId() + PrintColor.getNone() + SPAN
                        + PrintColor.getBlue() + getClassName() + PrintColor.getNone() + SPAN
                        + log);
    }

    private String getClassName(){
        String cls = clazz.getName();
        return cls.substring(cls.lastIndexOf(StringUtils.CURRENT_PATH) + 1);
    }

    public void warning(Object log){
        System.out.println(PrintColor.getRed() + LogLevel.WARNING + SPAN + getDate() + SPAN
                + Thread.currentThread().getId() + SPAN + clazz.getName() + SPAN
                + log + PrintColor.getRed());
    }

    public void error(Object log){
        System.out.println(PrintColor.getRed() + LogLevel.WARNING + SPAN + getDate() + SPAN
                + Thread.currentThread().getId() + SPAN + clazz.getName() + SPAN
                + log + PrintColor.getRed());
    }

    protected String getDate(){
        return SDF.format(new Date());
    }

    public static String getPid(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }
}
