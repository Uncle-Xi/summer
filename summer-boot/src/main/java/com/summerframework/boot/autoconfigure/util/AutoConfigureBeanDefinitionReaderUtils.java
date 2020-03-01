package com.summerframework.boot.autoconfigure.util;

import com.summerframework.beans.factory.config.BeanDefinition;
import com.summerframework.beans.factory.config.BeanDefinitionHolder;
import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.beans.factory.support.GenericBeanDefinition;
import com.summerframework.boot.autoconfigure.annotation.AnnotationAutoConfigureUtils;
import com.summerframework.context.DefaultListableBeanFactory;
import com.summerframework.core.logger.LogFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * @description: AutoConfigureBeanDefinitionReaderUtils
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AutoConfigureBeanDefinitionReaderUtils extends BeanDefinitionReaderUtils {


    private static final LogFactory logger = new LogFactory(AutoConfigureBeanDefinitionReaderUtils.class);

    public static void registerLocalBeanDefinition(DefaultListableBeanFactory beanFactory,
                                                   Set<String> locals) {
        doRegister(beanFactory, locals, false);
    }

    public static void registerFactoriesBeanDefinition(DefaultListableBeanFactory beanFactory,
                                                       Set<String> factories) {
        doRegister(beanFactory, factories, true);
    }

    public static void doRegister(DefaultListableBeanFactory beanFactory,
                                  Set<String> registerClass,
                                  boolean isFactories) {
        try {
            if (registerClass == null) {
                return;
            }
            for (String className : registerClass) {
                logger.info("[doRegister] [className]=[" + className + "].");
                if (!AnnotationAutoConfigureUtils.isPresent(className))      { continue; }
                Class<?> beanClass = Class.forName(className);
                if (!isBean(beanClass, isFactories))                         { continue; }
                List<Annotation> annotations = AnnotationAutoConfigureUtils.getAnnotationList(beanClass);
                if (!AnnotationAutoConfigureUtils.isConditionOn(beanFactory, annotations,
                        AnnotationAutoConfigureUtils.CONDITION_ANNOTATIONS)) { continue; }
                recursiveBeanAnnotationClass(annotations, beanFactory, true);
                recursiveBeanAnnotationScanPackage(annotations, beanFactory, isFactories);
                recursiveConfigurationAllBean(beanClass, annotations, beanFactory, true);
                BeanDefinition beanDefinition = getBeanDefinition(className);
                if (isEarlyBean(beanClass)) {
                    registerEarlyBeanDefinition(
                            new BeanDefinitionHolder(beanDefinition, beanDefinition.getBeanClassName(), null),
                            beanFactory);
                } else {
                    registerBeanDefinition(initBeanDefinitionHolder(beanDefinition, className, getBeanAliases(beanClass)), beanFactory);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void recursiveBeanAnnotationClass(List<Annotation> annotations,
                                                       DefaultListableBeanFactory beanFactory,
                                                       boolean isFactories){
        Set<String> annotationClassList =
                AnnotationAutoConfigureUtils.getAllAnnotationClass(annotations);
        if (annotationClassList != null) {
            doRegister(beanFactory, annotationClassList, isFactories);
        }
    }

    protected static void recursiveBeanAnnotationScanPackage(List<Annotation> annotations,
                                                       DefaultListableBeanFactory beanFactory,
                                                       boolean isFactories){
        Set<String> annotationClassList = AnnotationAutoConfigureUtils.getComponentScanAllClass(annotations);
        if (annotationClassList != null) {
            doRegister(beanFactory, annotationClassList, isFactories);
        }
    }

    protected static void recursiveConfigurationAllBean(Class<?> beanClass,
                                                        List<Annotation> annotations,
                                                        DefaultListableBeanFactory beanFactory,
                                                        boolean isFactories){
        Set<String> annotationClassList =
                AnnotationAutoConfigureUtils.getConfigurationAllBean(beanClass, annotations);
        if (annotationClassList != null) {
            doRegister(beanFactory, annotationClassList, isFactories);
        }
    }

    protected static boolean isEarlyBean(Class<?> beanClass){
        return AnnotationConfigUtils
                .isContentAnnotation(beanClass, AnnotationAutoConfigureUtils.EARLY_DI_CLASS_ANNOTATIONS);
    }

    protected static boolean isBean(Class<?> beanClass, boolean isFactories) {
        if (isFactories) {
            if (beanClass.isInterface()) {
                return false;
            }
        } else {
            if (beanClass.isInterface()
                    || !AnnotationConfigUtils.isContentAnnotation(beanClass, AnnotationAutoConfigureUtils.CLASS_ANNOTATIONS)) {
                return false;
            }
        }
        return true;
    }

    protected static BeanDefinition getBeanDefinition(String className){
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(className);
        beanDefinition.setFactoryBeanName(className);
        return beanDefinition;
    }


}
