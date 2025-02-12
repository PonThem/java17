package com.valuelab.common.auth;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoginContext {

    private final Map<String, LoginStrategy> loginStrategies;

    public LoginContext(Map<String, LoginStrategy> loginStrategies) {
        this.loginStrategies = loginStrategies;
    }

    public boolean login(String method, String username, String password) {
        LoginStrategy strategy = loginStrategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的登錄方式: " + method);
        }
        return strategy.login(username, password);
    }
}

