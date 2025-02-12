package com.valuelab.form;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
public class SearchUserForm {

    @Size(max = 128, message = "{user_id}{form_validation_size_error}")
    private String userId;

    @Size(max = 128, message = "{user_name}{form_validation_size_error}")
    private String userName;

    @Size(max = 319, message = "{mail_address}{form_validation_size_error}")
    private String mailAddress;

    @Pattern(regexp = "^[01]?$", message = "{user_type}{form_validation_pattern_error_please_specify_a_specific_value}")
    private String userType;
}
