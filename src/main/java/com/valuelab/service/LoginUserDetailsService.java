package com.valuelab.service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.valuelab.entity.MstUser;

public interface LoginUserDetailsService {
    /**
     * get user details by jwt
     * 
     * @param jwt
     * @return user info
     */
    public MstUser getUserInfoFromDB(Jwt jwt);

    /**
     * get user details
     * 
     * @return user details
     */
    public MstUser getUserInfo();
}
