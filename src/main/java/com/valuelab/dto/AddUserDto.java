package com.valuelab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AddUserDto {

    private String uuid;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private LocalDateTime updatedDatetime;

}
