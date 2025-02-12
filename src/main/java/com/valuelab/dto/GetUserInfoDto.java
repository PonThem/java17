package com.valuelab.dto;

import com.valuelab.entity.MstUser;

import lombok.Data;

@Data
public class GetUserInfoDto {

    public static GetUserInfoDto of(MstUser mstUser) {

        GetUserInfoDto dto = new GetUserInfoDto();
        if (mstUser == null) {
            return dto;
        }
        dto.setUserPk(mstUser.getUserPk().toString());
        dto.setUuid(mstUser.getUuid());
        dto.setUserId(mstUser.getUserId());
        dto.setUserName(mstUser.getUserName());
        dto.setMailAddress(mstUser.getMailAddress());
        dto.setUserType(mstUser.getUserType());
        return dto;
    }

    private String userPk;
    private String uuid;
    private String userId;
    private String userName;
    private String mailAddress;
    private String userType;
}
