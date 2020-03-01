package com.summerframework.beans.factory.support;

import com.summerframework.aop.aspectj.*;
import com.summerframework.aop.framework.AdvisedSupport;
import com.summerframework.aop.framework.AopProxy;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.beans.factory.annotation.Value;
import com.summerframework.context.annotation.Bean;
import com.summerframework.context.annotation.Component;
import com.summerframework.context.annotation.Configuration;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.StringUtils;
import com.summerframework.stereotype.Controller;
import com.summerframework.stereotype.Repository;
import com.summerframework.stereotype.Service;
import com.summerframework.transaction.annotation.Transactional;
import com.summerframework.web.bind.annotation.*;
import net.sf.cglib.proxy.Factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description: AnnotationConfigUtils
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public abstract class AnnotationConfigUtils {

    private static final LogFactory logger = new LogFactory(AnnotationConfigUtils.class);
    
    public static final Class<?>[] CLASS_ANNOTATIONS = {
            Controller.class,
            RestController.class,
            Service.class,
            Component.class,
            Repository.class,
            Configuration.class,
            Aspect.class
    };

    public static final Class<?>[] AUTO_CONFIGURATION_ANNOTATIONS = {Configuration.class};
    public static final Class<?>[] BEAN_ANNOTATIONS = {Bean.class};
    public static final Class<?>[] CONFIGURATION_ANNOTATIONS = {Configuration.class};
    public static final Class<?>[] VALUES_ANNOTATIONS = {Value.class};
    public static final Class<?>[] ASPECT_CLASS_ANNOTATIONS = {Aspect.class};
    public static final Class<?>[] ASPECT_POINT_CUT_ANNOTATIONS = {Pointcut.class};
    public static final Class<?>[] ASPECT_BEFORE_ANNOTATIONS = {Before.class};
    public static final Class<?>[] ASPECT_AFTER_ANNOTATIONS = {After.class};
    public static final Class<?>[] ASPECT_AFTER_RETURNING_ANNOTATIONS = {AfterReturning.class};
    public static final Class<?>[] ASPECT_AFTER_THROWING_ANNOTATIONS = {AfterThrowing.class};
    public static final Class<?>[] ASPECT_METHOD_ANNOTATIONS = {
            Before.class, After.class, AfterReturning.class, AfterThrowing.class};
    protected static Class<?>[] AUTOWIRED_ANNOTATIONS = {Autowired.class};

    public static final Class<?>[] CONTROLLER_ANNOTATIONS = {Controller.class,
            RestController.class
    };

    public static final Class<?>[] HANDLER_MAPPING_ANNOTATIONS = {
            RequestMapping.class,
            PostMapping.class,
            GetMapping.class
    };

    public static final Class<?>[] PARAM_ANNOTATIONS = {RequestParam.class};
    public static final Class<?>[] TRANSACTION_ANNOTATIONS = {Transactional.class};

    public static final String PARAM_VALUE_PREFIX = "value=";

    public static void main(String[] args) {
        //String value = getAnnotationValue(HiSummer.class, CONTROLLER_ANNOTATIONS);
        //logger.info(value);

        //logger.info();
        //for(Method method : HiSummer.class.getMethods()){
        //    String value = getAnnotationValue(method, HANDLER_MAPPING_ANNOTATIONS);
        //    logger.info(value);
        //}
    }

    public static boolean isContentAnnotation(Annotation annotation, Class<?>[] annotationClass) {
        return getAnnotation(annotationClass, annotation) != null;
    }

    public static boolean isContentAnnotation(Field field, Class<?>[] annotationClass) {
        return getContentAnnotation(field, annotationClass) != null;
    }

    public static boolean isContentAnnotation(Method method, Class<?>[] annotationClass) {
        return getContentAnnotation(method, annotationClass) != null;
    }

    public static boolean isContentAnnotation(Object object, Class<?>[] annotationClass) {
        return getContentAnnotation(object.getClass(), annotationClass) != null;
    }

    public static boolean isContentAnnotation(Class<?> beanClass, Class<?>[] annotationClass) {
        return getContentAnnotation(beanClass, annotationClass) != null;
    }

    public static String getAnnotationValue(Method method, Class<?>[] annotationClass) {
        Annotation annotation = getContentAnnotation(method, annotationClass);
        //logger.info("getAnnotationValue annotation.toString() >>> " + annotation.toString());
        return getAnnotationValue(annotation);
    }

    public static String getAnnotationValue(Field field, Class<?>[] annotationClass) {
        Annotation annotation = getContentAnnotation(field, annotationClass);
        return getAnnotationValue(annotation);
    }

    public static String getAnnotationValue(Object object, Class<?>[] annotationClass) {
        Annotation annotation = getContentAnnotation(object.getClass(), annotationClass);
        return getAnnotationValue(annotation);
    }

    public static String getBootsAnnotationValue(Annotation annotation) {
        String av = annotation.toString();
        av = av.substring(av.indexOf(PARAM_VALUE_PREFIX));
        if (av == null ) { return StringUtils.NONE_SPACE; }
        av = av.split(StringUtils.COMMA)[0].replace(PARAM_VALUE_PREFIX, StringUtils.NONE_SPACE);
        av = av.replaceAll("\\[|\\]", StringUtils.NONE_SPACE).trim();
        av = av.replaceAll("class ", StringUtils.NONE_SPACE).trim();
        av = av.replaceAll("\\)|\\(", StringUtils.NONE_SPACE);
        return av;
    }

    public static String getAnnotationValue(Annotation annotation) {
        String annotationValue = annotation.toString();
        annotationValue = annotationValue.substring(annotationValue.lastIndexOf(StringUtils.EQUALS) + 1);
        annotationValue = annotationValue.substring(0, annotationValue.length() - 1);
        return annotationValue.replaceAll("\\[", StringUtils.NONE_SPACE).replaceAll("\\]", StringUtils.NONE_SPACE);
    }

    public static String getParamAnnotationValue(Annotation annotation) {
        String annotationStr = annotation.toString();
        String[] annotationValues = annotationStr.substring(annotationStr.lastIndexOf("(")).split(StringUtils.COMMA);
        for (String value : annotationValues) {
            value = value.trim();
            if (value.startsWith(PARAM_VALUE_PREFIX)) {
                return value.replace(PARAM_VALUE_PREFIX, StringUtils.NONE_SPACE).trim();
            }
        }
        return null;
    }


    public static String getAspectMethodAnnotationValue(Annotation annotation) {
        String annotationStr = annotation.toString();
        String[] annotationValues =
                annotationStr.substring(annotationStr.indexOf("(")).split(StringUtils.COMMA);
        for (String value : annotationValues) {
            value = value.trim();
            if (value.startsWith(PARAM_VALUE_PREFIX)) {
                return value.replace(PARAM_VALUE_PREFIX, StringUtils.NONE_SPACE).trim()
                        .replaceAll("\\(\\(", "\\(")
                        .replaceAll("\\)\\)", "\\)");
            }
        }
        return null;
    }

    public static String getPointCutAnnotationValue(Annotation annotation) {
        String annotationStr = annotation.toString();
        String[] annotationValues =
                annotationStr.substring(annotationStr.lastIndexOf("Pointcut("))
                        .replaceAll("Pointcut\\(", StringUtils.NONE_SPACE)
                        .split(StringUtils.COMMA);
        for (String value : annotationValues) {
            value = value.trim();
            if (value.startsWith(PARAM_VALUE_PREFIX)) {
                return value.replace(PARAM_VALUE_PREFIX, StringUtils.NONE_SPACE).trim();
            }
        }
        return null;
    }


    public static Annotation getContentAnnotation(Field field, Class<?>[] annotationClass) {
        return getAnnotation(annotationClass, field.getDeclaredAnnotations());
    }

    public static Annotation getContentAnnotation(Method method, Class<?>[] annotationClass) {
        return getAnnotation(annotationClass, method.getAnnotations());
    }

    public static Annotation getContentAnnotation(Class<?> beanClass, Class<?>[] annotationClass) {
        return getAnnotation(annotationClass, beanClass.getAnnotations());
    }

    public static Annotation getAnnotation(Class<?>[] annotationClass, Annotation... sourceAnnotation) {
        for (Class<?> clazz : annotationClass) {
            for (Annotation annotation : sourceAnnotation) {
                if (annotation.annotationType().toString().contains(clazz.getName())) {
                    return annotation;
                }
            }
        }
        return null;
    }

    public static boolean isProxyBean(Class<?> targetBeanClazz){
        return Proxy.isProxyClass(targetBeanClazz)
                || instanceMethodInterceptor(targetBeanClazz);
    }

    public static Object getOriginalBean(Object bean) throws Exception {
        return AnnotationConfigUtils.instanceMethodInterceptor(bean.getClass())?
                AnnotationConfigUtils.getCglibProxyTargetObject(bean)
                :
                AnnotationConfigUtils.getJdkDynamicProxyTargetObject(bean);
    }

    public static boolean instanceMethodInterceptor(Class<?> targetBeanClazz){
        Class<?>[] interfaces = targetBeanClazz.getInterfaces();
        for(Class<?> clazz : interfaces){
            logger.info("instanceMethodInterceptor clazz.getName() -> " + clazz.getName());
            if (clazz == Factory.class){
                return true;
            }
        }
        return false;
    }

    public static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTarget();
        logger.info("getCglibProxyTargetObject done.");
        return target;
    }


    public static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTarget();
        logger.info("getJdkDynamicProxyTargetObject done.");
        return target;
    }

    public static void setJdkDynamicProxyTargetObject(Object proxy, Object newValue) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        ((AdvisedSupport) advised.get(aopProxy)).setTarget(newValue);
        logger.info("setJdkDynamicProxyTargetObject done.");
    }

    public static void addAutowiredAnnotations(Class<?> clazz){
        Class<?>[] newAutowiredAnnotations = new Class[AUTOWIRED_ANNOTATIONS.length + 1];
        for (int i = 0; i < AUTOWIRED_ANNOTATIONS.length; i++) {
            newAutowiredAnnotations[i] = AUTOWIRED_ANNOTATIONS[i];
        }
        newAutowiredAnnotations[AUTOWIRED_ANNOTATIONS.length] = clazz;
        AUTOWIRED_ANNOTATIONS = newAutowiredAnnotations;
    }
}
