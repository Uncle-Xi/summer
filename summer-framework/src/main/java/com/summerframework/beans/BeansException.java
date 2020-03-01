package com.summerframework.beans;

import com.summerframework.core.NestedRuntimeException;

public class BeansException extends NestedRuntimeException {

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException( String msg,  Throwable cause) {
        super(msg, cause);
    }

}
