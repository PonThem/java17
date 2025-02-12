package com.valuelab.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuelab.entity.MstUser;
import com.valuelab.service.LoginUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LogManager.getLogger(LogAspect.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoginUserDetailsService loginUserDetailsService;

    // アクセスログ
    @Before("execution(* *..*.*Controller.*(..))")
    public void startLog(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        // hide /heartbeat'g logs
        if ("/heartbeat".equals(request.getRequestURI())) {
            return;
        }

        MstUser user = loginUserDetailsService.getUserInfo();
        if (user == null) {
            MDC.put("user", "");
        } else {
            MDC.put("user", user.getUserId());
        }
        MDC.put("url", request.getRequestURL().toString());
        MDC.put("ip", request.getRemoteAddr());
        MDC.put("httpMethod", request.getMethod());
        MDC.put("method", joinPoint.getSignature().toString());

        if (request instanceof ContentCachingRequestWrapper) {
            String requestBody = new String(
                    ((ContentCachingRequestWrapper) request).getContentAsByteArray(),
                    StandardCharsets.UTF_8);
            logger.info("Request start: " + requestBody);
        } else {
            // リクエストボディがない場合（GET等）
            logger.info("Request start: Request Body is not cached");
        }
    }

    // 正常終了ログ
    @AfterReturning(pointcut = "execution(* *..*.*Controller.*(..))", returning = "result")
    public void afterReturningSample(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        // hide /heartbeat'g logs
        if ("/heartbeat".equals(request.getRequestURI())) {
            return;
        }

        try {
            String resultJson = objectMapper.writeValueAsString(result);
            logger.info("Request end: " + resultJson);
        } catch (Exception e) {
            logger.error("Request end: ", result);
        }
    }
}
