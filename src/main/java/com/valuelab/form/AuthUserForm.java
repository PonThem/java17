package com.valuelab.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

@Data
public class AuthUserForm {
    private String username;
    private String password;
    private String email;

    @NotNull(message = "{form_validation_notnull}")
    @Pattern(regexp = "^(admin|user)$", message = "Role can only be 'admin' or 'user'")
    private String role;
}
