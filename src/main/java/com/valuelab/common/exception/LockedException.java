package com.valuelab.common.exception;

public class LockedException extends RuntimeException {
    public LockedException() {
        super();
    }

    public LockedException(String message) {
        super(message);
    }
}
