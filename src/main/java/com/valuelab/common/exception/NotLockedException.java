package com.valuelab.common.exception;

public class NotLockedException extends RuntimeException {
    public NotLockedException() {
        super();
    }

    public NotLockedException(String message) {
        super(message);
    }
}
