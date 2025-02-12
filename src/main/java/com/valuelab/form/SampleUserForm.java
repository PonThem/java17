package com.valuelab.form;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SampleUserForm {

    private Long userId;
    private String cognitoSubId;
    private String role;

    @Email
    private String email;
    private String username;
    private String customUuid;

}
