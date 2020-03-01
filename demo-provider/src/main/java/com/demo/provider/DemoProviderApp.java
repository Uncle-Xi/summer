package com.demo.provider;

import com.summerframework.boot.SummerApplication;
import com.summerframework.boot.autoconfigure.SummerBootApplication;

/**
 * @description: DemoProviderApp
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@SummerBootApplication
public class DemoProviderApp {

    public static void main(String[] args) {
        SummerApplication.run(DemoProviderApp.class, args);
    }
}