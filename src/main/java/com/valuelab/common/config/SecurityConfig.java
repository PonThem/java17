package com.valuelab.common.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.valuelab.service.LoginUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${cognito.issuer.url}")
    private String issuerUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginUserDetailsService userRoleService)
            throws Exception {
        CustomJwtConverter customJwtConverter = new CustomJwtConverter(userRoleService);
        // add auth api : user is in Cognito, but is not in DB, can't access the api
        // (current : others api will authenticate by Cognito, won't be check at DB)
        http
                .addFilterBefore(contentTypeFilter(), CorsFilter.class) // Add ContentTypeFilter before CORS filter
                .cors(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        // Allow Prometheus to access the metrics endpoint
                        .requestMatchers("/actuator/prometheus").permitAll()
                        // Allow Swagger URLs
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/heartbeat").permitAll()
                        .requestMatchers("/api/all/**").permitAll()// 全般的なAPI
                        .requestMatchers("/api/all/monitoring").permitAll()
                        .requestMatchers("/api/all/savelog").permitAll()
                        .requestMatchers("/api/setting/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtConverter)))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler()));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUrl);
    }

    @Bean
    public ContentTypeFilter contentTypeFilter() {
        return new ContentTypeFilter();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
