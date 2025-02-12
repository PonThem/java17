package com.valuelab.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import com.valuelab.common.config.CustomJwtAuthenticationToken;
import com.valuelab.repository.MstUsersRepository;
import com.valuelab.entity.MstUser;

@Service
public class LoginUserDetailsServiceImpl implements LoginUserDetailsService {

    @Autowired
    MstUsersRepository mstUserRepository;

    @Override
    public MstUser getUserInfoFromDB(Jwt jwt) {
        MstUser userinfo = new MstUser();
        String customUuid = jwt.getClaimAsString("custom:uuid");
        userinfo = mstUserRepository.selectUserDetails(customUuid);
        return userinfo;
    }

    @Override
    public MstUser getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof CustomJwtAuthenticationToken) {
            MstUser user = ((CustomJwtAuthenticationToken) authentication).getUser();
            return user;
        }

        return null;
    }
}
