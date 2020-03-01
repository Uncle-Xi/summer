package com.summerframework.beans.factory.support;

import com.summerframework.beans.BeanWrapper;
import com.summerframework.beans.BeanWrapperImpl;
import com.summerframework.beans.BeansException;
import com.summerframework.beans.InitializingBean;
import com.summerframework.beans.factory.config.BeanDefinition;
import com.summerframework.core.config.BeanPostProcessor;
import com.summerframework.core.env.Environment;
import com.summerframework.core.logger.LogFactory;
import net.sf.cglib.proxy.Factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private static final LogFactory logger = new LogFactory(AbstractAutowireCapableBeanFactory.class);
    
    /**
     * Map of bean definition objects, keyed by bean name
     */
    protected final Map<String, BeanDefinition> earlyBeanDefinitionMap = new ConcurrentHashMap<>(16);

    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * List of bean definition names, in registration order
     */
    protected volatile Set<String> beanDefinitionNames = new HashSet<>(256);

    /**
     * Cache of early singleton objects: bean name to bean instance.
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * Disposable bean instances: bean name --> disposable instance
     */
    private final Map<String, Object> disposableBeans = new LinkedHashMap<String, Object>();

    /**
     * Map between containing bean names: bean name --> Set of bean names that the bean contains
     */
    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<String, Set<String>>(16);

    /**
     * Map between dependent bean names: bean name --> Set of dependent bean names
     */
    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<String, Set<String>>(64);

    /**
     * Map between depending bean names: bean name --> Set of bean names for the bean's dependencies
     */
    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<String, Set<String>>(64);

    @Override
    protected Object createBean(String beanClassName, BeanDefinition beanDefinition, Object args) {
        return doCreateBean(beanClassName, beanDefinition, (Object[]) args);
    }

    protected Object doCreateBean(String beanName, BeanDefinition mbd,  Object[] args) throws BeansException {
        BeanWrapperImpl instanceWrapper = this.createBeanInstance(beanName, mbd, args);
        initializeBean(beanName, instanceWrapper, mbd);
        this.populateBean(beanName, mbd, instanceWrapper);
        singletonObjects.put(mbd.getBeanClassName(), instanceWrapper.getWrappedInstance());
        earlySingletonObjects.remove(mbd.getBeanClassName());
        return singletonObjects.get(mbd.getBeanClassName());
    }

    protected BeanWrapperImpl createBeanInstance(String beanName, BeanDefinition mbd,  Object[] args) {
        try {
            Object object = earlySingletonObjects.get(beanName);
            if (object == null) {
                object = constructDependence(beanName);
                populatePropertiesValue(object);
            }
            BeanWrapperImpl beanWrapper = new BeanWrapperImpl(mbd);
            beanWrapper.setWrappedInstance(object, object);
            earlySingletonObjects.put(beanName, object);
            return beanWrapper;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Object constructDependence(String beanName) throws Exception {
        disposableBeans.put(beanName, Boolean.TRUE);
        Class<?> clazz = Class.forName(beanName);
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Object object;
        boolean exist0 = false;
        Constructor mpConstructor = null;
        int parmCnt = 0;
        for (Constructor constructor : constructors) {
            int pc = constructor.getParameterCount();
            if (pc == 0) { exist0 = true; }
            if (pc > parmCnt) {
                parmCnt = pc;
                mpConstructor = constructor;
            }
        }
        if (exist0) { //  || clazz.isInterface()
            object = clazz.newInstance();
        } else {
            mpConstructor.setAccessible(true);
            Class<?>[] parameterTypes = mpConstructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];
            int idx = 0;
            for (Class<?> parameterClass : parameterTypes) {
                args[idx++] = getConstructorParameterBean(parameterClass);
            }
            object = mpConstructor.newInstance(args);
        }
        disposableBeans.remove(beanName);
        return object;
    }

    protected Object getConstructorParameterBean(Class<?> parameterClass){
        String className = parameterClass.getName();
        Object object = disposableBeans.get(className);
        if (object == null) { // Boolean.TRUE
            return getBean(className);
        }
        throw new RuntimeException("Constructor Cycle Dependence...");
    }

    protected void populatePropertiesValue(Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        Environment environment = getEnvironment();
        for (Field field : fields) {
            if (AnnotationConfigUtils.isContentAnnotation(field, AnnotationConfigUtils.VALUES_ANNOTATIONS)) {
                try {
                    String fieldName =
                            AnnotationConfigUtils.getAnnotationValue(field, AnnotationConfigUtils.VALUES_ANNOTATIONS);
                    logger.info("environment -> " + environment);
                    Object value = environment.getProperty(fieldName);
                    logger.info("populatePropertiesValue value = [" + value + "], fieldName = [" + fieldName + "].");
                    if (value == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(object, value);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    protected void populateBean(String beanName, BeanDefinition mbd,  BeanWrapper bw) {
        // autowireByType(beanName, mbd, bw, newPvs);
        autowireByName(beanName, mbd, (BeanWrapperImpl) bw);
        // checkDependencies(beanName, mbd, filteredPds, pvs);
        applyPropertyValues(beanName, mbd, (BeanWrapperImpl) bw);
    }

    protected void autowireByName(String beanName, BeanDefinition mbd, BeanWrapperImpl bwi) {
        //String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, pvs);
        logger.info("beanName -> " + beanName);
        Object targetBean = bwi.getWrappedInstance();
        Class<?> targetBeanClazz = targetBean.getClass();
        try {
            if (AnnotationConfigUtils.isProxyBean(targetBeanClazz)) {
                this.proxyBeanReject(targetBean, mbd, bwi);
            } else {
                this.noneProxyBeanReject(targetBean, mbd, bwi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void proxyBeanReject(Object targetBean,
                                 BeanDefinition beanDefinition, BeanWrapperImpl bwi) throws Exception {
        Object originalBean = AnnotationConfigUtils.getOriginalBean(targetBean)/*AnnotationConfigUtils.instanceMethodInterceptor(targetBean.getClass())?
                AnnotationConfigUtils.getCglibProxyTargetObject(targetBean)
                :
                AnnotationConfigUtils.getJdkDynamicProxyTargetObject(targetBean)*/;
        noneProxyBeanReject(originalBean, beanDefinition, bwi);
    }

    private void noneProxyBeanReject(Object targetBean,
                                     BeanDefinition beanDefinition, BeanWrapperImpl bwi) throws Exception {
        populationField(targetBean, beanDefinition);
        populationConfigurationBean(targetBean, beanDefinition);
    }

    private void populationField(Object targetBean, BeanDefinition beanDefinition) throws Exception {
        Field[] fields = targetBean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (AnnotationConfigUtils.isContentAnnotation(field, AnnotationConfigUtils.AUTOWIRED_ANNOTATIONS)) {
                String fieldName = field.getType().getName();
                Object propertiesBean = earlySingletonObjects.get(getRealBeanName(fieldName));
                if (propertiesBean == null) {
                    logger.info("proxyBeanReject not find Bean in earlySingletonObjects className = [" + fieldName + "].");
                    propertiesBean = getBean(fieldName);
                } else {
                    addPostProcessBean(beanDefinition, field.getName());
                }
                field.setAccessible(true);
                field.set(targetBean, propertiesBean);
                //AnnotationConfigUtils.setJdkDynamicProxyTargetObject(targetBean, originalBean);
            }
        }
    }

    private void populationConfigurationBean(Object targetBean, BeanDefinition beanDefinition) throws Exception {
        if (!AnnotationConfigUtils.isContentAnnotation(targetBean, AnnotationConfigUtils.CONFIGURATION_ANNOTATIONS)) {
            return;
        }
        Method[] methods = targetBean.getClass().getMethods();
        for (Method method : methods) {
            if (AnnotationConfigUtils.isContentAnnotation(method, AnnotationConfigUtils.BEAN_ANNOTATIONS)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] args = new Object[parameterTypes.length];
                int index = 0;
                for(Class<?> paramClazz : parameterTypes){
                    String paramName = paramClazz.getName();
                    Object propertiesBean = earlySingletonObjects.get(getRealBeanName(paramName));
                    if (propertiesBean == null) {
                        logger.info("proxyBeanReject not find Bean in earlySingletonObjects className = [" + paramName + "].");
                        propertiesBean = getBean(paramName);
                    } else {
                        addPostProcessBean(beanDefinition, paramName);
                    }
                    args[index++] = propertiesBean;
                }
                method.setAccessible(true);
                method.invoke(targetBean, args);
            }
        }
    }

    private void addPostProcessBean(BeanDefinition mbd, String injectBeanName) {
        Set<String> dependenceEachOther = dependentBeanMap.get(mbd.getBeanClassName());
        if (dependenceEachOther == null) {
            dependenceEachOther = new HashSet<>();
        }
        dependenceEachOther.add(injectBeanName);
        dependentBeanMap.put(mbd.getBeanClassName(), dependenceEachOther);
    }


    private String getRealBeanName(String beanAnyName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanAnyName);
        return beanDefinition == null ? null : beanDefinition.getBeanClassName();
    }

    protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapperImpl bwi) {
        if (bwi.getPropertyValue().isEmpty()) {
            logger.info("Finish Dependence inject Bean = [" + mbd.getBeanClassName() + "].");
            singletonObjects.put(mbd.getBeanClassName(), bwi.getWrappedInstance());
            return;
        }
        //Class<?> clazz = bwi.getWrappedInstance().getClass();
        //Field[] fields = clazz.getDeclaredFields();
        //for (Field field : fields) {
        //    String fieldName = field.getType().getName();
        //    try {
        //        if (AnnotationConfigUtils.isContentAnnotation(field, AnnotationConfigUtils.AUTOWIRED_ANNOTATIONS)) {
        //            field.setAccessible(true);
        //            field.set(bwi.getWrappedInstance(), bwi.getPropertyValue().get(fieldName));
        //        }
        //    } catch (Exception e) {
        //        e.printStackTrace();
        //        System.out.printf("applyPropertyValues Exception filedName = [%s]\n", fieldName);
        //    }
        //}
    }

    @Override
    public void beforeProcessBeanFactory() {
        logger.info("beforeProcessBeanFactory earlyBeanDefinitionMap.size() -> " + earlyBeanDefinitionMap.size());
        if (earlyBeanDefinitionMap == null || earlyBeanDefinitionMap.isEmpty()) {
            return;
        }
        for (String beanName : earlyBeanDefinitionMap.keySet()) {
            //logger.info("beanName >>>>>>>>> " + beanName);
            BeanDefinition beanDefinition = earlyBeanDefinitionMap.get(beanName);
            getBean(beanDefinition.getBeanClassName());
        }
    }

    @Override
    public void postProcessEarlyBeanFactory() {
        logger.info("postProcessEarlyBeanFactory：" + singletonObjects.size());
        if (dependentBeanMap == null || dependentBeanMap.isEmpty()) {
            return;
        }
        for (String beanName : dependentBeanMap.keySet()) {
            logger.info("postProcessEarlyBeanFactory beanName >>> " + beanName);
            Object bean = singletonObjects.get(beanName);
            if (bean == null) {
                continue;
            }
            reInject(beanName, bean);
        }
    }

    private void reInject(String beanName, Object bean) {
        Class<?> clazz = bean.getClass();
        Object originalBean = bean;
        try {
            if (AnnotationConfigUtils.isProxyBean(clazz)) { // Proxy.isProxyClass(clazz) || instanceMethodInterceptor(clazz)
                originalBean = AnnotationConfigUtils.getOriginalBean(bean);
                /*AnnotationConfigUtils.instanceMethodInterceptor(bean.getClass())?
                        AnnotationConfigUtils.getCglibProxyTargetObject(bean): AnnotationConfigUtils.getJdkDynamicProxyTargetObject(bean)*/
                logger.info("proxy a ha");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        clazz = originalBean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getType().getName();
            if (AnnotationConfigUtils.isContentAnnotation(field, AnnotationConfigUtils.AUTOWIRED_ANNOTATIONS)) {
                try {
                    logger.info("postProcessEarlyBeanFactory fieldName -> " + fieldName);
                    Object injectBean = singletonObjects.get(fieldName);
                    if (injectBean == null) {
                        injectBean = singletonObjects.get(beanDefinitionMap.get(fieldName).getBeanClassName());
                        logger.info("none -> " + injectBean);
                    }
                    field.setAccessible(true);
                    field.set(originalBean, injectBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("postProcessEarlyBeanFactory Exception fieldName = [" + fieldName + "].");
                }
            }
        }
        if (AnnotationConfigUtils.isContentAnnotation(originalBean, AnnotationConfigUtils.CONFIGURATION_ANNOTATIONS)) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (AnnotationConfigUtils.isContentAnnotation(method, AnnotationConfigUtils.BEAN_ANNOTATIONS)) {
                    try {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Object[] args = new Object[parameterTypes.length];
                        int index = 0;
                        for(Class<?> paramClazz : parameterTypes){
                            String paramName = paramClazz.getName();
                            logger.info("postProcessEarlyBeanFactory fieldName -> " + paramName);
                            Object injectBean = singletonObjects.get(paramName);
                            if (injectBean == null) {
                                injectBean = singletonObjects.get(beanDefinitionMap.get(paramName).getBeanClassName());
                                logger.info("none -> " + injectBean);
                            }
                            args[index++] = injectBean;
                        }
                        method.setAccessible(true);
                        method.invoke(originalBean, args);
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.info("postProcessEarlyBeanFactory exception methodName = [" + method.getName() + "]");
                    }
                }
            }
        }
        singletonObjects.put(beanName, bean);
    }

    protected Object initializeBean(final String beanName, final BeanWrapperImpl instanceWrapper,  BeanDefinition mbd) {
        Object wrappedBean = instanceWrapper.getWrappedInstance();
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        try {
            invokeInitMethods(beanName, wrappedBean, mbd);
        } catch (Throwable ex) {
            throw new RuntimeException(beanName + "Invocation of init method failed", ex);
        }
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        instanceWrapper.setWrappedInstance(wrappedBean, instanceWrapper.getWrappedInstance());
        return wrappedBean;
    }

    protected void invokeInitMethods(String beanName, final Object bean,  BeanDefinition mbd)
            throws Throwable {
        boolean isInitializingBean = (bean instanceof InitializingBean);
        if (isInitializingBean && mbd != null) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }

    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }
}
