package com.summerframework.context;

import com.summerframework.beans.Aware;
import com.summerframework.beans.BeansException;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
