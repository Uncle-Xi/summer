package com.summerframework.aop.autoproxy;

import com.summerframework.aop.framework.AdvisedSupport;
import com.summerframework.aop.framework.AopConfiguration;
import com.summerframework.aop.framework.CglibAopProxy;
import com.summerframework.aop.framework.JdkDynamicAopProxy;
import com.summerframework.beans.Aware;
import com.summerframework.beans.BeanFactory;
import com.summerframework.core.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAdvisorAutoProxyCreator implements BeanPostProcessor, Aware {

    private AopConfiguration aopConfiguration    = new AopConfiguration();
    private List<String> beanNames               = new ArrayList<String>();
    protected static final Object[] DO_NOT_PROXY = null;
    protected static final Object[] PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS = new Object[0];

    @SuppressWarnings("rawtypes")
    protected Object[] getAdvicesAndAdvisorsForBean(Class beanClass, String beanName, Object targetSource) {
        for (String mappedName : this.beanNames) {
            if (!mappedName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
                continue;
            }
            mappedName = mappedName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
            if (isMatch(beanName, mappedName)) {
                return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;
            }
        }
        return DO_NOT_PROXY;
    }

    @SuppressWarnings({ "rawtypes" })
    protected Object createProxy(Object targetSource, String beanName) {
        //System.out.println("createProxy = " + beanName);
        //System.out.println("DefaultAdvisorAutoProxyCreator createProxy this " + this);
        //System.out.println("DefaultAdvisorAutoProxyCreator createProxy this " + aopConfiguration);
        //System.out.println("DefaultAdvisorAutoProxyCreator createProxy this " + aopConfiguration.getPointCut2MethodMapping().size());
        try {
            Class targetClass = targetSource.getClass();
            AdvisedSupport advisedSupport = new AdvisedSupport(aopConfiguration, targetSource.getClass(), targetSource);
            if(targetClass.getInterfaces().length > 0){
                return new JdkDynamicAopProxy(advisedSupport).getProxy();
            }
            return new CglibAopProxy(advisedSupport).getProxy();
        } catch (Throwable ex) {
            System.out.println("createProxy Throwable ...");
            ex.printStackTrace();
        }
        return targetSource;
    }

    public AopConfiguration getAopConfiguration() {
        return aopConfiguration;
    }

//    public void setAopConfiguration(AopConfiguration aopConfiguration) {
//        this.aopConfiguration = aopConfiguration;
//    }

    protected boolean isMatch(String beanName, String mappedName) {
        return simpleMatch(mappedName, beanName);
    }

    public static boolean simpleMatch(String pattern, String str) {
        if (pattern != null && str != null) {
            int firstIndex = pattern.indexOf(42);
            if (firstIndex == -1) {
                return pattern.equals(str);
            } else if (firstIndex == 0) {
                if (pattern.length() == 1) {
                    return true;
                } else {
                    int nextIndex = pattern.indexOf(42, firstIndex + 1);
                    if (nextIndex == -1) {
                        return str.endsWith(pattern.substring(1));
                    } else {
                        String part = pattern.substring(1, nextIndex);
                        for(int partIndex = str.indexOf(part); partIndex != -1; partIndex = str.indexOf(part, partIndex + 1)) {
                            if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                                return true;
                            }
                        }
                        return false;
                    }
                }
            } else {
                return str.length() >= firstIndex && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) && simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex));
            }
        } else {
            return false;
        }
    }

    public static boolean simpleMatch(String[] patterns, String str) {
        if (patterns != null) {
            for(int i = 0; i < patterns.length; ++i) {
                if (simpleMatch(patterns[i], str)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected abstract void initConfiguration();
}
