package com.valuelab.common.config;

import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContentTypeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String contentType = request.getContentType();
            if (contentType == null || (!contentType.contains("application/json") &&
                    !contentType.contains("application/x-www-form-urlencoded") &&
                    !contentType.startsWith("multipart/form-data"))) {
                // status is 415 UNSUPPORTED_MEDIA_TYPE
                response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                response.getWriter().write("Unsupported Media Type");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
