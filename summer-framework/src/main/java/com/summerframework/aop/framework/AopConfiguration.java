package com.summerframework.aop.framework;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @description: AopConfiguration
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class AopConfiguration {

    public static final String ADVISED_METHOD_SIGN_BEFORE              = "before";
    public static final String ADVISED_METHOD_SIGN_AFTER               = "after";
    public static final String ADVISED_METHOD_SIGN_AFTER_RETURNING     = "afterReturning";
    public static final String ADVISED_METHOD_SIGN_AFTER_THROWING      = "afterThrowing";

    public static final String SUMMER_AOP_POINTCUT_PREFIX              = "summer.aop.pointcut";
    public static final String SUMMER_AOP_ASPECT_CLASS_PREFIX          = "summer.aop.aspect";
    public static final String SUMMER_AOP_ASPECT_METHOD_BEFORE         = "summer.aop.method.before";
    public static final String SUMMER_AOP_ASPECT_METHOD_AFTER          = "summer.aop.method.after";
    public static final String SUMMER_AOP_ASPECT_METHOD_AFTER_THROW    = "summer.aop.method.after.throwing";
    public static final String SUMMER_AOP_ASPECT_EXCEPTION             = "summer.aop.exception";

    private Map<String, Object> aspectBeanMap               = new HashMap<>();
    private Map<String, Set<Method>> pointCut2MethodMapping = new HashMap<>();
    private Properties properties                           = new Properties();
    private boolean initedPointCut2AspectMethod = false;
    public Properties getProperties() {
        return properties;
    }
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    public String getAdvisedMethodSignBefore() {
        return ADVISED_METHOD_SIGN_BEFORE;
    }

    public Map<String, Set<Method>> getPointCut2MethodMapping() {
        return pointCut2MethodMapping;
    }

    public void setPointCut2MethodMapping(Map<String, Set<Method>> pointCut2MethodMapping) {
        this.pointCut2MethodMapping = pointCut2MethodMapping;
    }

    public Object getPointCutObject(String key) {
        if (aspectBeanMap == null) {
            return null;
        }
        return aspectBeanMap.get(key);
    }

    public void setAspectBeanMap(Map<String, Object> pointCutObjectMap) {
        this.aspectBeanMap = pointCutObjectMap;
    }

    public Map<String, Set<Method>> initPointCutToAspectMethod() {
        if (initedPointCut2AspectMethod
                && (pointCut2MethodMapping != null && !pointCut2MethodMapping.isEmpty())) {
            return pointCut2MethodMapping;
        }
        initedPointCut2AspectMethod = true;
        Map<String, Set<Method>> pc2m = new HashMap<>();
        for(String pointCut : pointCut2MethodMapping.keySet()){
            //System.out.println("pointCut2MethodMapping = pointCut " + pointCut);
            //Object object = aspectBeanMap.get(pointCut);
            //if (object == null) {
            //    System.out.println("initPointCutToAspectMethod object == null ");
            //    continue;
            //}
            //if (object.getClass().getName().equals(pointCut)) {
            //    pc2m.put(pointCut, pointCut2MethodMapping.get(pointCut));
            //    System.out.println("initPointCutToAspectMethod object.getClass().getName().equals(pointCut) "
            //            + object.getClass().getName().equals(pointCut));
            //    continue;
            //}
            String key = pointCut.replaceAll("\\.", "\\\\.")
                    .replaceAll("\\\\.\\*", ".*")
                    .replaceAll("\\(", "\\\\(")
                    .replaceAll("\\)", "\\\\)")
                    /*.replaceAll("\\\\", "\\\\\\\\")*/;
            pc2m.put(key, pointCut2MethodMapping.get(pointCut));
        }
        // System.out.println("pointCut2MethodMapping = pc2m " + pc2m.size());
        // System.out.println("pointCut2MethodMapping = pc2m " + pointCut2MethodMapping.size());
        // pointCut2MethodMapping.clear();
        // for (String key : pc2m.keySet()) {
        //     pointCut2MethodMapping.put(key, pc2m.get(key));
        // }
        // System.out.println("pointCut2MethodMapping = pc2m " + pointCut2MethodMapping.size());
        return pointCut2MethodMapping = pc2m;
    }
}
