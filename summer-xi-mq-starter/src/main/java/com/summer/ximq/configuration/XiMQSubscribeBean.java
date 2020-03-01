package com.summer.ximq.configuration;

import com.summer.ximq.XiMQConfig;
import com.summerframework.beans.Aware;
import com.summerframework.beans.BeansException;
import com.summerframework.beans.InitializingBean;
import com.summerframework.beans.factory.annotation.Autowired;
import com.summerframework.beans.factory.support.AnnotationConfigUtils;
import com.summerframework.boot.autoconfigure.AnnotationAutoConfigureApplicationContext;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ApplicationContextAware;
import com.summerframework.context.ApplicationEventPublisher;
import com.summerframework.context.ApplicationListener;
import com.summerframework.context.annotation.Bean;
import com.summerframework.context.event.SourceFilteringListener;
import com.summerframework.context.support.AbstractApplicationContext;
import com.summerframework.core.annotation.Order;
import com.summerframework.core.env.ConfigurableEnvironment;
import com.summerframework.core.logger.LogFactory;
import com.summerframework.web.context.event.ContextRefreshedEvent;
import com.ximq.common.annotation.XiMQListener;
import com.ximq.common.config.ClientConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @description: XiMQSubscribe
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Order
public class XiMQSubscribeBean extends XiMQConfig implements InitializingBean, ApplicationContextAware,
        ApplicationListener<ContextRefreshedEvent>, Aware, ApplicationEventPublisher {

    private static final LogFactory logger = new LogFactory(XiMQSubscribeBean.class);

    private AbstractApplicationContext applicationContext;
    private ConfigurableEnvironment environment;

    @Autowired
    XiMQProperties properties;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AbstractApplicationContext) applicationContext;
        this.environment = this.applicationContext.getEnvironment();
        this.publishEvent(this);
    }

    @Override
    public void publishEvent(Object o) {
        this.applicationContext.
                addApplicationListener(new SourceFilteringListener(applicationContext, (ApplicationListener<?>) o));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    Set<String> classNames = new HashSet<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanMame : beanDefinitionNames) {
            try {
                Class<?> beanClass = Class.forName(beanMame);
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    for (Annotation annotation : method.getAnnotations()) {
                        if (annotation.annotationType().getName().equals(XiMQListener.class.getName())) {
                            classNames.add(beanMame);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (classNames.isEmpty()) {
            logger.info("[onApplicationEvent] [XiMQListener classNames.isEmpty()]");
            return;
        }
        Properties properties = getProperty(this.properties);
        for (String clazz : classNames) {
            Object bean = applicationContext.getBean(clazz);
            try {
                Class<?> beanClass = Class.forName(clazz);
                if (AnnotationConfigUtils.isProxyBean(beanClass)) {
                    bean = AnnotationConfigUtils.getOriginalBean(bean);
                    logger.info("[onApplicationEvent] [这是一个代理Bean] - [" + clazz);
                }
                Method[] methods = bean.getClass().getMethods();
                for (Method method : methods) {
                    for (Annotation annotation : method.getAnnotations()) {
                        if (annotation.annotationType().getName().equals(XiMQListener.class.getName())) {
                            AnnotationValues values = parseAnnotation(annotation);
                            if (values == null) {
                                break;
                            }
                            if (values.groupId != null && !values.groupId.equalsIgnoreCase("")) {
                                properties.setProperty(ClientConfig.XIMQ_GROUP_ID, values.groupId);
                            }
                            xiMQSubscribe.doSubscribe(properties, values.topics, bean, method);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private AnnotationValues parseAnnotation(Annotation annotation){
        AnnotationValues values = new AnnotationValues();
        values.groupId = ((XiMQListener)annotation).groupId();
        String[] topics = ((XiMQListener)annotation).topics();
        values.topics = topics == null? null : new HashSet<>(Arrays.asList(topics));
        return values;
    }

    static class AnnotationValues{
        String groupId;
        Set<String> topics;
    }
}
