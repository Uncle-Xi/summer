package com.summerframework.beans;

import java.util.Map;

public interface BeanWrapper {

    void setPropertyValues(Map<?, ?> map) throws BeansException;

}
