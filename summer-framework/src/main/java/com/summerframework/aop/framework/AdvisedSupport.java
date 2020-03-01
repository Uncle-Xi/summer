package com.summerframework.aop.framework;

import com.summerframework.aop.aspectj.AspectJAfterReturningAdvice;
import com.summerframework.aop.aspectj.AspectJAfterThrowingAdvice;
import com.summerframework.aop.interceptor.MethodAfterAdviceInterceptor;
import com.summerframework.aop.interceptor.MethodBeforeAdviceInterceptor;
import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.transaction.interceptor.TransactionInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @description: AdvisedSupport
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AdvisedSupport {

    private static final LogFactory logger = new LogFactory(AdvisedSupport.class);
    private Class<?> targetClass;
    private Object target;
    private AopConfiguration aopConfiguration;
    private transient Map<Method, List<Object>> methodCache = new HashMap<>();

    public boolean advisedIsEmpty() {
        return methodCache.isEmpty();
    }

    public Object getTarget() {
        return this.target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public AdvisedSupport(AopConfiguration aopConfiguration,
                          Class<?> targetClass, Object target) {
        this.aopConfiguration = aopConfiguration;
        this.targetClass = targetClass;
        this.target = target;
        initInterceptorsChain();
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);
        //System.out.println("[method] - [" + method);
        //System.out.println("[cached] - [" + cached);
        if (cached == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            //System.out.println("[Method] - [" + m.getName());
            this.methodCache.put(m, cached);
        }
        return cached;
    }


//    private static void print() {
//        for (int i = 0; i < 10; i++) {
//            logger.info("\n");
//        }
//    }

    private void initInterceptorsChain() {
        Map<String, Method> methodSigns = getMethodSignList();
        Map<String, Set<Method>> pointCutToAspectMethod = aopConfiguration.getPointCut2MethodMapping();
        if (methodSigns == null
                || pointCutToAspectMethod == null
                || pointCutToAspectMethod.size() < 1
                || methodSigns.size() < 1) {
            return;
        }
        //logger.info("initInterceptorsChain in ... ");
        for (String methodSign : methodSigns.keySet()) {
            for (String pointCut : pointCutToAspectMethod.keySet()) {
                if (isMatch(pointCut, methodSign)
                        || AnnotationConfigUtils.isContentAnnotation(targetClass, AnnotationConfigUtils.TRANSACTION_ANNOTATIONS)) {
                    this.addAdvised(methodSigns.get(methodSign), pointCutToAspectMethod.get(pointCut));
                }
            }
        }
    }


    private boolean isMatch(String pointCut, String methodSign) {
        //Pattern pattern = Pattern.compile(pointCut);
        //Matcher matcher = pattern.matcher(methodSign);
        //logger.info("matcher.matches() pointCut >>> " + pointCut);
        //logger.info("matcher.matches() methodSign >>> " + methodSign);
        //logger.info("matcher.matches() >>> " + matcher.matches());
        //logger.info("matcher.matches() >>> " + Pattern.matches(pointCut, methodSign));
        //return matcher.matches();
        return Pattern.matches(pointCut, methodSign);
    }

    public static void main(String[] args) {
//        String pointCut = "public .* com\\.acceptance\\.mvc\\.servlet\\.service.*\\(.*\\)";
//        String method = "public java.lang.String com.acceptance.mvc.servlet.service.impl.HiServiceImpl.messageHandler(java.lang.String)";
//        logger.info(pointCut);
//        logger.info(Pattern.matches(pointCut, method));
//        pointCut = pointCut.replaceAll("\\\\", "\\\\\\\\");
//        logger.info(pointCut);
//        logger.info(Pattern.matches(pointCut, method));

//        pointCut = "public .* com.acceptance.mvc.servlet.service.*(.*)";
//        String key = pointCut.replaceAll("\\.", "\\\\.")
//                .replaceAll("\\\\.\\*", ".*")
//                .replaceAll("\\(", "\\\\(")
//                .replaceAll("\\)", "\\\\)")
                /*.replaceAll("\\\\", "\\\\\\\\")*/;

//        logger.info(pointCut);
//        logger.info(Pattern.matches(pointCut, method));
//        Map<String, Method> methodSigns = new HashMap<>();
//        methodSigns.put(key, null);
//        for (String s : methodSigns.keySet()) {
//            logger.info(Pattern.matches(s, method));
//        }

        String methodSign = "public java.lang.String java.lang.Object.toString()";
    }

    private Map<String, Method> getMethodSignList() {
        Map<String, Method> methodMap = new HashMap<>();
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            String methodSign = method.toString();
            if (this.doNotNeedAdvised(methodSign)) {
                continue;
            }
            if (methodSign.contains("throws")) {
                methodSign = methodSign.substring(0, methodSign.lastIndexOf("throws")).trim();
            }
            methodMap.put(methodSign, method);
        }
        return methodMap;
    }

    private void addAdvised(Method method, Set<Method> methods) {
        List<Object> advices = new LinkedList<Object>();
        for (Method advised : methods) {
            if (doNotNeedAdvised(advised.toString())) {
                continue;
            }
            for (Annotation annotation : advised.getAnnotations()) {
                //logger.info("methods.size() >>> " + methods.size());
                if (annotation != null) {
                    logger.info("annotation.annotationType().toString() >>> " + annotation.annotationType().toString());
                }
            }
            if (AnnotationConfigUtils.isContentAnnotation(advised, AnnotationConfigUtils.ASPECT_BEFORE_ANNOTATIONS)) {
                logger.info("before >>> " + advised.getName());
                advices.add(new MethodBeforeAdviceInterceptor(advised, this.getAdvisedBean(advised)));
            }
            if (advised.getName().equals(AopConfiguration.ADVISED_METHOD_SIGN_AFTER)) {
                logger.info("after >>> " + advised.getName());
                advices.add(new MethodAfterAdviceInterceptor(advised, this.getAdvisedBean(advised)));
            }
            if (AnnotationConfigUtils.isContentAnnotation(advised, AnnotationConfigUtils.ASPECT_AFTER_RETURNING_ANNOTATIONS)) {
                logger.info("afterReturning >>> " + advised.getName());
                advices.add(new AspectJAfterReturningAdvice(advised, this.getAdvisedBean(advised)));
            }
            if (AnnotationConfigUtils.isContentAnnotation(advised, AnnotationConfigUtils.ASPECT_AFTER_THROWING_ANNOTATIONS)) {
                logger.info("afterThrowing >>> " + advised.getName());
                advices.add(new AspectJAfterThrowingAdvice(advised, this.getAdvisedBean(advised)));
            }
            if (AnnotationConfigUtils.isContentAnnotation(advised, AnnotationConfigUtils.TRANSACTION_ANNOTATIONS)
                    || AnnotationConfigUtils.isContentAnnotation(targetClass, AnnotationConfigUtils.TRANSACTION_ANNOTATIONS)) {
                logger.info("advised >>> " + advised.getName());
                advices.add(new TransactionInterceptor(advised, null));
            }
        }
        methodCache.put(method, advices);
    }

    private Object getAdvisedBean(Method advised) {
        String className = advised.getDeclaringClass().getName();
        logger.info(className);
        return aopConfiguration.getPointCutObject(className);
    }


    private boolean doNotNeedAdvised(String methodSign) {
        if (methodSign.contains("java.lang.Object.hashCode")) {
            return true;
        }
        if (methodSign.contains("java.lang.Object.wait")) {
            return true;
        }
        if (methodSign.contains("java.lang.Object.equals")) {
            return true;
        }
        if (methodSign.contains("java.lang.Object.toString")) {
            return true;
        }
        if (methodSign.contains("java.lang.Object.getClass")) {
            return true;
        }
        if (methodSign.contains("java.lang.Object.notify")) {
            return true;
        }
        //System.out.println("[methodSign] - [" + methodSign);
        return false;
    }
}
