package com.valuelab.common.exception;

public class ExclusiveException extends RuntimeException {
    public ExclusiveException() {
        super();
    }

    public ExclusiveException(String message) {
        super(message);
    }
}
