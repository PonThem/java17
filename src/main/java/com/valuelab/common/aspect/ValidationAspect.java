package com.valuelab.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.valuelab.common.exception.ValidationException;

@Aspect
@Component
public class ValidationAspect {
    @Before("@within(org.springframework.web.bind.annotation.RestController)")
    public void validateBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;
                if (bindingResult.hasErrors()) {
                    String message = bindingResult.getFieldError().getDefaultMessage();
                    throw new ValidationException(message);
                }
            }
        }
    }
}