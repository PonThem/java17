package com.valuelab.common.auth;

import org.springframework.stereotype.Component;

@Component("auth0LoginStrategy")
public class Auth0LoginStrategy implements LoginStrategy {

    @Override
    public boolean login(String username, String password) {
        System.out.println("使用 Auth0 登錄");
        // 調用 Auth0 API 驗證用戶身份
        return true; // 模擬驗證成功
    }
}
