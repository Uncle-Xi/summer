package com.demo.complete;

import com.summerframework.boot.SummerApplication;
import com.summerframework.boot.autoconfigure.SummerBootApplication;

/**
 * @description: hi demo
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@SummerBootApplication
public class DemoApp {

    public static void main(String[] args) {
        SummerApplication.run(DemoApp.class, args);
    }
}
