package com.valuelab.dto;

import com.valuelab.form.SampleForm;
import lombok.Data;

@Data
public class SampleDto {

    private String userId;
    private String userName;

    public static SampleDto of(SampleForm sampleForm) {

        SampleDto dto = new SampleDto();

        dto.setUserId(sampleForm.getUserId());
        dto.setUserName(sampleForm.getUserName());

        return dto;
    }
}
