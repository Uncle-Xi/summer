package com.summerframework.core;

public abstract class NestedRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -4107795080604134449L;

    public NestedRuntimeException(String msg) {
        super(msg);
    }

    public NestedRuntimeException( String msg,  Throwable cause) {
        super(msg, cause);
    }

    @Override

    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }


    public Throwable getRootCause() {
        return NestedExceptionUtils.getRootCause(this);
    }

}
