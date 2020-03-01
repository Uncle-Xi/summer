package com.demo.complete.aop;

import com.summerframework.aop.Joinpoint;
import com.summerframework.aop.aspectj.*;
import com.summerframework.core.logger.LogFactory;

import java.util.Arrays;

/**
 * @description: LogAspect
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Aspect
public class LogAspect {

    private static final LogFactory logger = new LogFactory(LogAspect.class);

    @Pointcut(value = "public .* com.demo.complete.service.*(.*)")
    public void pointCut(){}

    @Before(value = "pointCut()")
    public void before(Joinpoint joinPoint) {
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(), System.currentTimeMillis());
        logger.info("Before -> TargetObject = [" + joinPoint.getThis()
                        + "]; Args = [" + Arrays.toString(joinPoint.getArguments()) + "]...");
    }

    @After(value = "pointCut()")
    public void after(Joinpoint joinPoint) {
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        logger.info("After -> TargetObject = [" + joinPoint.getThis()
                + "]; Args = [" + Arrays.toString(joinPoint.getArguments())
                + "]; Use Time = [" + (endTime - startTime) + "]");
    }
}
