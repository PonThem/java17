package com.valuelab.common.auth;

public interface LoginStrategy {
    boolean login(String username, String password);
}
