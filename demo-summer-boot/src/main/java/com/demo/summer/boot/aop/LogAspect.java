package com.demo.summer.boot.aop;

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

    @Pointcut(value = "public .* com.demo.summer.boot.service.*(.*)")
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

    @AfterReturning(value = "pointCut()")
    public void afterReturning(Joinpoint joinPoint) {
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        logger.info("AfterReturning -> TargetObject = [" + joinPoint.getThis()
                + "]; Args = [" + Arrays.toString(joinPoint.getArguments())
                + "]; Use Time = [" + (endTime - startTime) + "]");
    }

    @AfterThrowing(value = "pointCut()")
    public void afterThrowing(Joinpoint joinPoint, Throwable ex) {
        logger.info("AfterThrowing -> TargetObject = [" + joinPoint.getThis()
                + "]; Args = [" + Arrays.toString(joinPoint.getArguments())
                + "]; Throws = [" + ex.getMessage() + "]");
    }
}
