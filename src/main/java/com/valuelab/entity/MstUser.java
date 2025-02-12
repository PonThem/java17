package com.valuelab.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MstUser {
    private Long userPk;
    private String userId;
    private String userName;
    private String userType;
    private String mailAddress;
    private String uuid;
    private LocalDateTime createdDatetime;
    private int createdUserPk;
    private LocalDateTime updatedDatetime;
    private int updatedUserPk;
    boolean isDeleted;
}
