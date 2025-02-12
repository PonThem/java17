package com.valuelab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valuelab.common.config.CustomJwtAuthenticationToken;
import com.valuelab.dto.GetUserInfoDto;
import com.valuelab.entity.MstUser;
import com.valuelab.repository.MstUsersRepository;

@Service
@Transactional
public class GetUserInfoServiceImpl implements GetUserInfoService {

    @Autowired
    MstUsersRepository mstUsersRepository;

    public GetUserInfoDto getUserInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MstUser user = null;
        if (authentication instanceof CustomJwtAuthenticationToken) {
            user = ((CustomJwtAuthenticationToken) authentication).getUser();
        }
        return GetUserInfoDto.of(user);
    }
}
