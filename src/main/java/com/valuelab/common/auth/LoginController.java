package com.valuelab.common.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginContext loginContext;

    public LoginController(LoginContext loginContext) {
        this.loginContext = loginContext;
    }

    @PostMapping
    public String login(@RequestParam String method,
                        @RequestParam String username,
                        @RequestParam String password) {
        boolean success = loginContext.login(method, username, password);
        return success ? "Login Successful" : "Login Failed";
    }
}

