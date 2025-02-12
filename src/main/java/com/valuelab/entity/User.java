package com.valuelab.entity;

import lombok.Data;

import java.io.Serializable;

import com.valuelab.form.SampleUserForm;

@Data
public class User implements Serializable {

    public static User of(SampleUserForm userForm) {

        User user = new User();

        user.setUserId(userForm.getUserId());
        user.setCognitoSubId(userForm.getCognitoSubId());
        user.setRole(userForm.getRole());
        user.setEmail(userForm.getEmail());
        user.setUsername(userForm.getUsername());
        user.setCustomUuid(userForm.getCustomUuid());

        return user;
    }

    private Long userId;
    private String cognitoSubId;
    private String role;
    private String email;
    private String username;
    private String customUuid;

}
