package com.valuelab.common.config;

import java.util.Collection;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.valuelab.entity.MstUser;

public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {

    private final transient MstUser user;

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, MstUser user) {
        super(jwt, authorities);
        this.user = user;
    }

    public MstUser getUser() {
        return user;
    }
}