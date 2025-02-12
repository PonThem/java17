package com.valuelab.common.config;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Collection;
import java.util.Collections;

import com.valuelab.service.LoginUserDetailsService;
import com.valuelab.entity.MstUser;

public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final LoginUserDetailsService userRoleService;
    private static final Logger logger = LoggerFactory.getLogger(CustomJwtConverter.class);

    public CustomJwtConverter(LoginUserDetailsService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // String sub = jwt.getSubject(); // Get the 'sub' claim
        String uuid = jwt.getClaimAsString("custom:uuid");
        MstUser userinfo = new MstUser();
        if (!uuid.isEmpty()) {
            userinfo = userRoleService.getUserInfoFromDB(jwt);
        }

        Collection<GrantedAuthority> authorities;

        try {
            MDC.put("userId", uuid);
            boolean isAdmin = userinfo.getUserType().equals("1");
            boolean isSystemManager = userinfo.getUserType().equals("9");
            logger.info(uuid + " is admin: " + isAdmin);
            logger.info(uuid + " is isSystemManager: " + isSystemManager);
            if (isAdmin || isSystemManager) {
                authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities = Collections.emptyList(); // Or assign a default role if applicable
            }
        } catch (Exception e) {
            // Log the exception and handle it according to your application's policy
            logger.error("Error checking admin role", e);
            // For security, treat it as a non-admin if the role cannot be verified
            authorities = Collections.emptyList();
        }

        return new CustomJwtAuthenticationToken(jwt, authorities, userinfo);
    }
}
