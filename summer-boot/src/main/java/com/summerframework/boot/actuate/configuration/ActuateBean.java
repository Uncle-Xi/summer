package com.summerframework.boot.actuate.configuration;

import com.summerframework.beans.BeansException;
import com.summerframework.beans.FactoryBean;
import com.summerframework.beans.InitializingBean;
import com.summerframework.boot.actuate.mbean.HealthMBean;
import com.summerframework.boot.actuate.mbean.Health;
import com.summerframework.context.ApplicationContext;
import com.summerframework.context.ApplicationContextAware;
import com.summerframework.context.annotation.ComponentScan;
import com.summerframework.context.support.AbstractApplicationContext;
import com.summerframework.core.annotation.Order;
import com.summerframework.core.config.BeanPostProcessor;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @description: ActuateBean
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
@Order
@ComponentScan(value = {"com.summerframework.boot.actuate.controller"})
public class ActuateBean implements ApplicationContextAware, BeanPostProcessor, InitializingBean, FactoryBean {

    ObjectName objectName;
    HealthMBean healthMBean;

    private AbstractApplicationContext applicationContext;
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AbstractApplicationContext) applicationContext;
        this.healthMBean = new Health(this.applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registerControllerBean();
        registerMBean();
    }

    private void registerControllerBean() {
        applicationContext.registerBeanAndDefinition(HealthMBean.class.getName(), this.healthMBean);
    }

    private void registerMBean() {
        try {
            objectName = new ObjectName("com.summerframework.boot.actuate.mbean.Health:type=Health");
            mBeanServer.registerMBean(this.healthMBean, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
