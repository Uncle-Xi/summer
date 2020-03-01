package com.summerframework.transaction.interceptor;

import com.summerframework.aop.Joinpoint;
import com.summerframework.aop.MethodInterceptor;
import com.summerframework.aop.MethodInvocation;
import com.summerframework.aop.aspectj.AbstractAspectJAdvice;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: TransactionInterceptor
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class TransactionInterceptor extends AbstractAspectJAdvice implements MethodInterceptor {

    private Joinpoint joinPoint;
    private static final String OPEN_TRANSACTION_KEY = "OPEN_TRANSACTION_KEY";
    private ThreadLocal<Map<String, Boolean>> openTransactionMap = new ThreadLocal<>();

    public TransactionInterceptor(Method aspectJAdviceMethod, Object aspectTarget) {
        super(aspectJAdviceMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            openTransaction();
            return invocation.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            closeTransaction();
        }
    }

    private void openTransaction(){
        Map<String, Boolean> autoCommit = openTransactionMap.get();
        if (autoCommit == null) {
            autoCommit = new HashMap<>();
            autoCommit.put(OPEN_TRANSACTION_KEY, Boolean.TRUE);
        }
    }

    private void closeTransaction(){
        Map<String, Boolean> autoCommit = openTransactionMap.get();
        if (autoCommit == null) {
            openTransactionMap.remove();
        }
    }
}
