package com.demo.consumer;

import com.summerframework.boot.SummerApplication;
import com.summerframework.boot.autoconfigure.SummerBootApplication;

/**
 * @description: DemoConsumerApp
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@SummerBootApplication
public class DemoConsumerApp {

    public static void main(String[] args) {
        SummerApplication.run(DemoConsumerApp.class, args);
    }
}
