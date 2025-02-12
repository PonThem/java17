package com.valuelab.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.valuelab.entity.MstUser;

import lombok.Data;

@Data
public class SearchUserDto {

    public static SearchUserDto of(MstUser mstUser) {
        SearchUserDto dto = new SearchUserDto();
        dto.setUuid(mstUser.getUuid());
        dto.setUserId(mstUser.getUserId());
        dto.setUserName(mstUser.getUserName());
        dto.setMailAddress(mstUser.getMailAddress());
        dto.setUserType(mstUser.getUserType());
        dto.setUpdatedDatetime(mstUser.getUpdatedDatetime());
        return dto;
    }

    private String uuid;
    private String userId;
    private String userName;
    private String mailAddress;
    private String userType;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    private LocalDateTime updatedDatetime;

}
