package com.valuelab.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AddUserForm {

    @NotNull(message = "{user_id}{form_validation_notnull}")
    @NotEmpty(message = "{user_id}{form_validation_notnull}")
    @Pattern(regexp = "\\d+", message = "{user_id}{form_validation_type_error}")
    @Size(max = 128, message = "{user_id}{form_validation_size_error}")
    private String userId;

    @NotNull(message = "{user_name}{form_validation_notnull}")
    @NotEmpty(message = "{user_name}{form_validation_notnull}")
    @Size(max = 128, message = "{user_name}{form_validation_size_error}")
    private String userName;

    @NotNull(message = "{mail_address}{form_validation_notnull}")
    @NotEmpty(message = "{mail_address}{form_validation_notnull}")
    @Size(max = 319, message = "{mail_address}{form_validation_size_error}")
    private String mailAddress;

    @NotNull(message = "{user_type}{form_validation_notnull}")
    @NotEmpty(message = "{user_type}{form_validation_notnull}")
    @Pattern(regexp = "^[01]$", message = "{user_type}{form_validation_pattern_error_please_specify_a_specific_value}")
    private String userType;
}
