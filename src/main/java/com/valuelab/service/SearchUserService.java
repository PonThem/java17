package com.valuelab.service;

import com.valuelab.dto.SearchUserDtos;

public interface SearchUserService {
    public SearchUserDtos searchUser(String userId, String userName, String maillAddress, String userType);
}
