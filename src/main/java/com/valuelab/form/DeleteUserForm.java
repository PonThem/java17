package com.valuelab.form;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeleteUserForm {

    @NotNull(message = "{uuid}{form_validation_notnull}")
    @Size(max = 36, message = "{uuid}{form_validation_size_error}")
    private String uuid;

    @NotNull(message = "{updated_datetime}{form_validation_notnull}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedDatetime;
}
