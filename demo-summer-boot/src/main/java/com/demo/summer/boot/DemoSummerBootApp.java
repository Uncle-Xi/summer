package com.demo.summer.boot;

import com.summerframework.boot.SummerApplication;
import com.summerframework.boot.autoconfigure.SummerBootApplication;

/**
 * @description: DemoSummerBootApp
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@SummerBootApplication
public class DemoSummerBootApp {

    public static void main(String[] args) {
        SummerApplication.run(DemoSummerBootApp.class, args);
    }
}
