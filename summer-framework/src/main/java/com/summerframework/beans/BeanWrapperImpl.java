package com.summerframework.beans;

import com.summerframework.core.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class BeanWrapperImpl implements BeanWrapper{

    private Map<Object, Object> propertyValue = new HashMap<>();


    Object wrappedObject;


    Object rootObject;

    public BeanWrapperImpl(Object object) {
        this.rootObject = object;
        this.wrappedObject = object;
    }

    public void setWrappedInstance(Object object,  Object rootObject) {
        Assert.notNull(this.wrappedObject, "Target object must not be null");
        this.wrappedObject = object;
        this.rootObject = rootObject;
    }

    public final Object getWrappedInstance() {
        Assert.state(this.wrappedObject != null, "No wrapped object");
        return this.wrappedObject;
    }

    public Map<Object, Object> getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Object key, Object value) {
        propertyValue.put(key, value);
    }

    @Override
    public void setPropertyValues(Map<?, ?> map) throws BeansException {
        this.propertyValue = propertyValue;
    }
}
