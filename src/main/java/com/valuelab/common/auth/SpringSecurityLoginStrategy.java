package com.valuelab.common.auth;

import org.springframework.stereotype.Component;

@Component("springSecurityLoginStrategy")
public class SpringSecurityLoginStrategy implements LoginStrategy {

    @Override
    public boolean login(String username, String password) {
        System.out.println("使用 Spring Security 登錄");
        // 調用 Spring Security 的用戶驗證
        return true; // 模擬驗證成功
    }
}

