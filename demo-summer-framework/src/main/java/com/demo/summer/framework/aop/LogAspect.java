package com.demo.summer.framework.aop;

import com.summerframework.aop.Joinpoint;
import com.summerframework.aop.aspectj.*;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @description: LogAspect
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Aspect
public class LogAspect {

    private static final Logger logger = Logger.getLogger(LogAspect.class.toString());

    @Pointcut(value = "public .* com.demo.summer.framework.mvc.servlet.service.*(.*)")
    public void pointCut(){}

    @Before(value = "pointCut()")
    public void before(Joinpoint joinPoint) {
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(), System.currentTimeMillis());
        System.out.printf("Before -> TargetObject = [%s]; Args = [%s]...\n",
                joinPoint.getThis(),
                Arrays.toString(joinPoint.getArguments()));
    }

    @After(value = "pointCut()")
    public void after(Joinpoint joinPoint) {
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.printf("After -> TargetObject = [%s]; Args = [%s]; Use Time = [%s]...\n",
                joinPoint.getThis(),
                Arrays.toString(joinPoint.getArguments()),
                (endTime - startTime));
    }

    @AfterReturning(value = "pointCut()")
    public void afterReturning(Joinpoint joinPoint) {
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.printf("AfterReturning -> TargetObject = [%s]; Args = [%s]; Use Time = [%s]...\n",
                joinPoint.getThis(),
                Arrays.toString(joinPoint.getArguments()),
                (endTime - startTime));
    }

    @AfterThrowing(value = "pointCut()")
    public void afterThrowing(Joinpoint joinPoint, Throwable ex) {
        System.out.printf("AfterThrowing -> TargetObject = [%s]; Args = [%s]; Throws = [%s]...\n",
                joinPoint.getThis(),
                Arrays.toString(joinPoint.getArguments()),
                ex.getMessage());
    }
}
