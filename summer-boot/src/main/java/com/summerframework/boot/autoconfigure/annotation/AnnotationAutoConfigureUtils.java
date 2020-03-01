package com.summerframework.boot.autoconfigure.annotation;

import com.summerframework.aop.aspectj.Aspect;
import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.beans.factory.support.BeanDefinitionReaderUtils;
import com.summerframework.boot.autoconfigure.ConditionalOnClass;
import com.summerframework.boot.autoconfigure.ConditionalOnProperty;
import com.summerframework.boot.autoconfigure.EnableAutoConfiguration;
import com.summerframework.boot.autoconfigure.properties.ConfigurationProperties;
import com.summerframework.boot.autoconfigure.properties.EnableConfigurationProperties;
import com.summerframework.context.DefaultListableBeanFactory;
import com.summerframework.context.annotation.*;
import com.summerframework.core.annotation.Order;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.type.AnnotationMetadata;
import com.summerframework.core.type.StandardAnnotationMetadata;
import com.summerframework.core.util.StringUtils;
import com.summerframework.stereotype.Controller;
import com.summerframework.stereotype.Repository;
import com.summerframework.stereotype.Service;
import com.summerframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @description: AnnotationConfigUtils
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AnnotationAutoConfigureUtils extends AnnotationConfigUtils {

    public static final Class<?>[] CLASS_ANNOTATIONS = {
            Controller.class,
            RestController.class,
            Service.class,
            Component.class,
            Repository.class,
            Configuration.class,
            Aspect.class,
            EnableAutoConfiguration.class,
            ConfigurationProperties.class,
            EnableConfigurationProperties.class,
            ConditionalOnClass.class,
            Import.class,
            ConditionalOnProperty.class,
            ComponentScan.class,
            Bean.class,
    };

    public static final Class<?>[] ANNOTATION_VALUE_ANNOTATION = {
            Import.class,
            ComponentScan.class,
            ConditionalOnClass.class,
            ConditionalOnProperty.class,
            EnableConfigurationProperties.class
    };

    public static final Class<?>[] EARLY_DI_CLASS_ANNOTATIONS = {
            Order.class, Aspect.class, ConfigurationProperties.class
    };

    public static final Class<?>[] CONDITION_ANNOTATIONS = {ConditionalOnClass.class, ConditionalOnProperty.class};

    public static void main(String[] args) {
        try {
            //SummerBoot summerBoot = new SummerBoot();
            //List<Annotation> list = getAnnotationList(SummerBoot.class);

            //List<String> list = parseAnnotation(SummerBoot.class);
            //for(Annotation an : list){
            //    System.out.println(an.toString());
            //}
            //for (Annotation annotation : list) {
            //    String value = getBootAnnotationValue(annotation);
            //    System.out.println(annotation.toString());
            //    System.out.println(value);
            //    System.out.println();
            //}

            //String clazzName = "com.acceptance.boot.annotation.SummerBoot$CacheConfigurationImportSelector";
            //Class<?> clazz = Class.forName(clazzName);
            //for(Class<?> cli : clazz.getInterfaces()){
            //    if (cli == ImportSelector.class) {
            //        System.out.println("ok");
            //        Constructor<?> con = clazz.getDeclaredConstructor();
            //        con.setAccessible(true);
            //        Object object = con.newInstance();
            //        //Object object = clazz.newInstance();
            //        for (Method method : clazz.getMethods()) {
            //            System.out.println(method.getName());
            //        }
            //        Method method = clazz.getDeclaredMethod("selectImports", AnnotationMetadata.class);
            //        method.setAccessible(true);
            //        String[] arr = (String[]) method.invoke(object, new StandardAnnotationMetadata());
            //        Stream.of(arr).forEach(a -> System.out.println(a));
            //    }
            //}

            //Set<String> list = getConfigurationBeanList(SummerBoot.class);
            //for(String an : list){
            //    System.out.println(an);
            //}

            String annotation = "com.summerframework.context.annotation.ComponentScan";
            System.out.println(annotation.equals(ComponentScan.class.getName()));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isPresent(String className) {
        try {
            return null != Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean isConditionOn(DefaultListableBeanFactory beanFactory,
                                        List<Annotation> annotations,
                                        Class<?>[] annotationClass){
        ConfigurableEnvironment environment = (ConfigurableEnvironment) beanFactory.getEnvironment();
        for (Annotation annotation : annotations) {
            for (Class<?> ac : annotationClass) {
                if (annotation.toString().contains(ac.getName())) {
                    String value = getBootAnnotationValue(annotation);
                    if (annotation.toString().contains(ConditionalOnProperty.class.getName())) {
                        if (!environment.contentKey(value)) {
                            return false;
                        }
                    } else {
                        if (!isPresent(value)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    protected static String getBootAnnotationValue(Annotation annotation){
        if (!isContentAnnotation(annotation, ANNOTATION_VALUE_ANNOTATION)) {
            return StringUtils.NONE_SPACE;
        }
        return getBootsAnnotationValue(annotation);
    }

    protected static List<String> handlerValue(String value){
        List<String> values = new ArrayList<>();
        if (value != null) {
            Stream.of(value.split(StringUtils.COMMA)).forEach(v -> values.add(v.trim()));
        }
        return values;
    }

    public static Set<String> getAllAnnotationClass(List<Annotation> annotations){
        Set<String> classList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (isContentAnnotation(annotation, new Class<?>[]{Import.class, EnableConfigurationProperties.class})) {
                List<String> cls = handlerValue(getBootAnnotationValue(annotation));
                classList.addAll(isImportSelector(cls));
            }
        }
        return classList;
    }

    public static List<String> isImportSelector(List<String> cls){
        List<String> classList = new ArrayList<>();
        for(String clazzName : cls){
            try {
                boolean isSelectImports = false;
                Class<?> clazz = Class.forName(clazzName);
                for (Class<?> cif : clazz.getInterfaces()) {
                    if (cif == ImportSelector.class) {
                        Constructor<?> con = clazz.getDeclaredConstructor();
                        con.setAccessible(true);
                        Object object = con.newInstance();
                        Method method = clazz.getDeclaredMethod("selectImports", AnnotationMetadata.class);
                        method.setAccessible(true);
                        String[] classArr = (String[]) method.invoke(object, new StandardAnnotationMetadata());
                        Stream.of(classArr).forEach(cs -> {
                            classList.add(cs);
                        });
                        isSelectImports = true;
                    }
                }
                if (!isSelectImports){
                    classList.add(clazzName);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return classList;
    }

    public static Set<String> getComponentScanAllClass(List<Annotation> annotations){
        Set<String> beanClass = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(ComponentScan.class.getName())) {
                List<String> basePackages = handlerValue(getBootAnnotationValue(annotation));
                int size = BeanDefinitionReaderUtils.SCAN_PACKAGES.size();
                BeanDefinitionReaderUtils.SCAN_PACKAGES.addAll(basePackages);
                if (size == BeanDefinitionReaderUtils.SCAN_PACKAGES.size()) { return beanClass; }
                beanClass.addAll(BeanDefinitionReaderUtils.scanPackages(basePackages));
            }
        }
        return beanClass;
    }



    public static Set<String> getConfigurationAllBean(Class<?> beanClass,
                                                       List<Annotation> annotations){
        Set<String> beanList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(Configuration.class.getName())) {
                beanList.addAll(getConfigurationBeanList(beanClass));
            }
        }
        return beanList;
    }

    protected static Set<String> getConfigurationBeanList(Class<?> beanClass){
        Set<String> beanList = new HashSet<>();
        Method[] methods = beanClass.getMethods();
        for(Method method : methods){
            for (Annotation annotation : method.getAnnotations()){
                if (annotation.annotationType().getName().equals(Bean.class.getName())){
                    beanList.add(method.getReturnType().getName());
                }
            }
        }
        return beanList;
    }

    public static List<Annotation> getAnnotationList(Class<?> clazz) {
        List<Annotation> annotationList = new ArrayList<>();
        if (clazz == null)                             { return null; }
        try {
            ClassLoader classLoader = clazz.getClassLoader();
            Annotation[] annotations = clazz.getAnnotations();
            doParseAnnotationReturnAnnotation(annotations, classLoader, annotationList);
        } catch (Exception e) { e.printStackTrace(); }
        return annotationList;
    }

//    public static List<Annotation> getAnnotationList(Object object) {
//        List<Annotation> annotationList = new ArrayList<>();
//        if (object == null)                             { return null; }
//        try {
//            ClassLoader classLoader = object.getClass().getClassLoader();
//            Annotation[] annotations = object.getClass().getAnnotations();
//            doParseAnnotationReturnAnnotation(annotations, classLoader, annotationList);
//        } catch (Exception e) { e.printStackTrace(); }
//        return annotationList;
//    }

//    public static List<String> parseAnnotation(Class<?> clazz) {
//        List<String> annotationList = new ArrayList<>();
//        if (clazz == null)                             { return null; }
//        try {
//            ClassLoader classLoader = clazz.getClassLoader();
//            Annotation[] annotations = clazz.getAnnotations();
//            doParseAnnotation(annotations, classLoader, annotationList);
//        } catch (Exception e) { e.printStackTrace(); }
//        return annotationList;
//    }

//    public static List<String> parseAnnotation(Object object) {
//        List<String> annotationList = new ArrayList<>();
//        if (object == null)                             { return null; }
//        try {
//            ClassLoader classLoader = object.getClass().getClassLoader();
//            Annotation[] annotations = object.getClass().getAnnotations();
//            doParseAnnotation(annotations, classLoader, annotationList);
//        } catch (Exception e) { e.printStackTrace(); }
//        return annotationList;
//    }

    private static void doParseAnnotationReturnAnnotation(Annotation[] annotations,
                                          ClassLoader classLoader,
                                          List<Annotation> annotationList) throws ClassNotFoundException {
        if (annotations == null)                                          { return; }
        for (Annotation annotation : annotations) {
            String annotationType = annotation.annotationType().getName();
            String annotationValue = annotation.toString();
            if (ignore(annotationType, annotationValue))              { continue; }
            annotationList.add(annotation);
            Annotation[] at = classLoader.loadClass(annotationType).getAnnotations();
            doParseAnnotationReturnAnnotation(at, classLoader, annotationList);
        }
    }

//    private static void doParseAnnotation(Annotation[] annotations,
//                                          ClassLoader classLoader,
//                                          List<String> annotationList) throws ClassNotFoundException {
//        if (annotations == null)                                          { return; }
//        for (Annotation annotation : annotations) {
//            String annotationType = annotation.annotationType().getName();
//            String annotationValue = annotation.toString();
//            if (ignore(annotationType, annotationValue))              { continue; }
//            annotationList.add(annotationValue);
//            Annotation[] at = classLoader.loadClass(annotationType).getAnnotations();
//            doParseAnnotation(at, classLoader, annotationList);
//        }
//    }

    private static boolean ignore(String annotationType, String annotationValue){
        if (!annotationValue.contains("@"))                                                  { return true; }
        if ("com.summerframework.context.annotation.Conditional".equals(annotationType))     { return true; }
        if ("java.lang.annotation.Target".equals(annotationType))                            { return true; }
        if ("java.lang.annotation.Documented".equals(annotationType))                        { return true; }
        if ("java.lang.annotation.Inherited".equals(annotationType))                         { return true; }
        if ("java.lang.annotation.Retention".equals(annotationType))                         { return true; }
        if (annotationType.contains("java.lang.annotation"))                                 { return true; }
        return false;
    }
}
