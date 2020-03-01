package com.summerframework.aop.autoproxy;

import com.summerframework.beans.BeansException;
import com.summerframework.beans.factory.config.BeanDefinition;
import com.summerframework.beans.factory.config.ConfigurableListableBeanFactory;
import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.core.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @description: DefaultAdvisorAutoProxyCreator
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class DefaultAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator {

    private static final LogFactory logger = new LogFactory(DefaultAdvisorAutoProxyCreator.class);
    private ConfigurableListableBeanFactory beanFactory;
    private ConfigurableEnvironment environment;
    private Map<String, BeanDefinition> earlyBeanDefinitionMap;

    public DefaultAdvisorAutoProxyCreator(ConfigurableListableBeanFactory beanFactory, ConfigurableEnvironment environment) {
        logger.info("DefaultAdvisorAutoProxyCreator this " + this);
        this.beanFactory = beanFactory;
        this.environment = environment;
        initConfiguration();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return wrapIfNecessary(bean, beanName);
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        //Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
        //if (specificInterceptors != null) {
        //    Object proxy = createProxy(bean, beanName);
        //    return proxy;
        //}
        //List<String> pointCutMatchList = getAopConfiguration().getPointCutMatchList();
        //if (pointCutMatchList == null || pointCutMatchList.isEmpty()){
        //    return bean;
        //}
        //for (String pointCat : pointCutMatchList) {
        //    logger.info("bean.getClass().getName() >>> " + bean.getClass().getName());
        //    logger.info("bean.getClass().getName() pointCat >>> " + pointCat);
        //    if (Pattern.matches(pointCat, bean.getClass().getName())){
        //        return createProxy(bean, beanName);
        //    }
        //}
        //return bean;
        return createProxy(bean, beanName);
    }

    @Override
    protected void initConfiguration() {
        try {
            //AopConfiguration aopConfiguration = new AopConfiguration();
            //String aspectClass = properties.getProperty(AopConfiguration.SUMMER_AOP_ASPECT_CLASS_PREFIX);
            //String pointcut = properties.getProperty(AopConfiguration.SUMMER_AOP_POINTCUT_PREFIX);
            earlyBeanDefinitionMap = beanFactory.getEarlyBeanDefinitionMap();
            if (earlyBeanDefinitionMap != null && !earlyBeanDefinitionMap.isEmpty()) {
                Map<String, Object> aspectBeanMap = getAspectBeanMap();
                this.getAopConfiguration().setAspectBeanMap(aspectBeanMap);
                this.getAopConfiguration().setPointCut2MethodMapping(getPointCut2MethodMapping(aspectBeanMap));
            } else {
                logger.info("initConfiguration earlyBeanDefinitionMap == null || earlyBeanDefinitionMap.isEmpty()");
            }
            this.getAopConfiguration().setProperties(environment.getDefaultProperties());
            this.getAopConfiguration().initPointCutToAspectMethod();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getAspectBeanMap() {
        Map<String, Object> aspectBeanMap = new HashMap<>();
        for (String beanName : earlyBeanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = earlyBeanDefinitionMap.get(beanName);
            Object object = beanFactory.getBean(beanDefinition.getBeanClassName());
            if (object != null
                    && AnnotationConfigUtils.isContentAnnotation(object, AnnotationConfigUtils.ASPECT_CLASS_ANNOTATIONS)) {
                aspectBeanMap.put(beanDefinition.getBeanClassName(), object);
            }
        }
        return aspectBeanMap;
    }

    private Map<String, Set<Method>> getPointCut2MethodMapping(Map<String, Object> aspectBeanMap) {
        Map<String, Set<Method>> pointCut2MethodMapping = new HashMap<>();
        Map<String, String> pointCutMapping = getPointCutMap(aspectBeanMap);
        for (String beanName : aspectBeanMap.keySet()) {
            Object aspect = aspectBeanMap.get(beanName);
            Class<?> clazz = aspect.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (AnnotationConfigUtils.isContentAnnotation(method, AnnotationConfigUtils.ASPECT_METHOD_ANNOTATIONS)) {
                    Annotation annotation =
                            AnnotationConfigUtils.getContentAnnotation(method, AnnotationConfigUtils.ASPECT_METHOD_ANNOTATIONS);
                    String pointCutId = AnnotationConfigUtils.getAspectMethodAnnotationValue(annotation);
                    pointCutId = pointCutId.contains(clazz.getName()) == false ?
                            (clazz.getName() + StringUtils.CURRENT_PATH + pointCutId) : pointCutId;
                    String key = pointCutMapping.get(pointCutId);
                    Set<Method> methodSet = pointCut2MethodMapping.get(key);
                    if (methodSet == null) {
                        methodSet = new HashSet<>();
                    }
                    methodSet.add(method);
                    logger.info("pointCutMapping.get(pointCutId) >>> " + pointCutMapping.get(pointCutId));
                    logger.info("methodSet.pointCutId >>> " + pointCutId);
                    logger.info("methodSet.size() >>> " + methodSet.size());
                    logger.info("method >>> " + method.getName());
                    pointCut2MethodMapping.put(key, methodSet);
                }
            }
        }
        return pointCut2MethodMapping;
    }

    private Map<String, String> getPointCutMap(Map<String, Object> aspectBeanMap) {
        Map<String, String> pointCutMapping = new HashMap<>();
        for (String beanName : aspectBeanMap.keySet()) {
            Object aspect = aspectBeanMap.get(beanName);
            Class<?> clazz = aspect.getClass();
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (AnnotationConfigUtils.isContentAnnotation(method, AnnotationConfigUtils.ASPECT_POINT_CUT_ANNOTATIONS)) {
                    Annotation annotation =
                            AnnotationConfigUtils.getContentAnnotation(method, AnnotationConfigUtils.ASPECT_POINT_CUT_ANNOTATIONS);
                    if (annotation == null) {
                        continue;
                    }
                    String pointCutId = aspect.getClass().getName() + StringUtils.CURRENT_PATH + method.getName() + "()";
                    String pointCutValue = AnnotationConfigUtils.getPointCutAnnotationValue(annotation);
                    pointCutMapping.put(pointCutId, pointCutValue);
                }
            }
        }
        return pointCutMapping;
    }

    public static void main(String[] args) {
//        Object object = new LogAspect();
//        Class<?> clazz = object.getClass();
//        Method[] methods = clazz.getMethods();
//        for (Method method : methods) {
//            String pointCutId = object.getClass().getName() + StringUtils.CURRENT_PATH + method.getName();
//            Annotation annotation = AnnotationConfigUtils.getContentAnnotation(method, AnnotationConfigUtils.ASPECT_POINT_CUT_ANNOTATIONS);
//            if (annotation == null) {
//                continue;
//            }
//            logger.info(pointCutId);
//            logger.info(annotation.toString());
//            String annotationStr = annotation.toString();
//            String[] annotationValues =
//                    annotationStr.substring(annotationStr.lastIndexOf("Pointcut("))
//                            .replaceAll("Pointcut\\(", "").split(StringUtils.COMMA);
//            for (String value : annotationValues) {
//                value = value.trim();
//                //logger.info(value);
//                if (value.startsWith("value=")) {
//                    logger.info(value.replace("value=", StringUtils.NONE_SPACE).trim());
//                }
//            }

//            if (AnnotationConfigUtils.isContentAnnotation(method, AnnotationConfigUtils.ASPECT_METHOD_ANNOTATIONS)) {
//                Annotation annotation =
//                        AnnotationConfigUtils.getContentAnnotation(method, AnnotationConfigUtils.ASPECT_METHOD_ANNOTATIONS);
//                String annotationStr = annotation.toString();
//                logger.info(annotationStr);
//                String[] annotationValues =
//                        annotationStr.substring(annotationStr.indexOf("(")).split(StringUtils.COMMA);
//                for (String value : annotationValues) {
//                    value = value.trim();
//                    if (value.startsWith("value=")) {
//                        value = value.replace("value=", StringUtils.NONE_SPACE).trim()
//                                .replaceAll("\\(\\(", "\\(")
//                                .replaceAll("\\)\\)", "\\)");
//                        logger.info(value);
//                    }
//                }
//            }
//        }
    }
}
