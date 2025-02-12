package com.valuelab.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserForm {

    @NotNull(message = "{uuid}{form_validation_notnull}")
    @NotEmpty(message = "{uuid}{form_validation_notnull}")
    @Size(max = 36, message = "{uuid}{form_validation_size_error}")
    private String uuid;

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
    @Pattern(regexp = "[01]", message = "{user_type}{form_validation_pattern_error_please_specify_a_specific_value}")
    private String userType;

    @NotNull(message = "{updated_datetime}{form_validation_notnull}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedDatetime;
}
