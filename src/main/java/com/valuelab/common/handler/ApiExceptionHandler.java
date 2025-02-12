package com.valuelab.common.handler;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.valuelab.common.util.CreateResponseFactory;
import com.valuelab.service.LoginUserDetailsService;
import com.valuelab.dto.ResponseBodyDto;
import com.valuelab.common.exception.DuplicatedCompanyException;
import com.valuelab.common.exception.DuplicatedSettlementException;
import com.valuelab.common.exception.DuplicatedUserException;
import com.valuelab.common.exception.ExclusiveException;
import com.valuelab.common.exception.HealthCheckException;
import com.valuelab.common.exception.LockedException;
import com.valuelab.common.exception.NotLockedException;
import com.valuelab.common.exception.ValidationDisplayOrderException;
import com.valuelab.common.exception.ValidationException;
import com.valuelab.dto.ErrorDetailDto;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    LoginUserDetailsService loginUserDetailsService;

    @Autowired
	protected MessageSource messageSource;

    private static final Logger apiExceptionHandlerLogger = LogManager.getLogger(
            ApiExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleException(Exception e) {
        apiExceptionHandlerLogger.error("handleException: {}", e.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("90");

        String exceptionSystemError = messageSource.getMessage("exception_system_error", null, Locale.JAPAN);
        errorDetailDto.setMessage(exceptionSystemError);

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleValidationException(ValidationException ex) {
        apiExceptionHandlerLogger.debug("Validation error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("04");
        errorDetailDto.setMessage(ex.getMessage());

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(ValidationDisplayOrderException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleValidationDisplayOrderException(
            ValidationDisplayOrderException ex) {
        apiExceptionHandlerLogger.debug("Validation error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("04");

        String exceptionValidationDisplayOrder = messageSource.getMessage("exception_validation_display_order", null, Locale.JAPAN);
        errorDetailDto.setMessage(exceptionValidationDisplayOrder);

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(HealthCheckException.class)
    public ResponseEntity<String> handleHealthCheckException(HealthCheckException ex) {
        apiExceptionHandlerLogger.error("HealthCheck error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(ex.getMessage());
    }

    // for app exception
    @ExceptionHandler(NotLockedException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleNotLockedException(NotLockedException ex) {
        apiExceptionHandlerLogger.error("NotLocked error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("01");
        errorDetailDto.setMessage(ex.getMessage());

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleLockedException(LockedException ex) {
        apiExceptionHandlerLogger.error("Locked error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("02");
        errorDetailDto.setMessage(ex.getMessage());

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(ExclusiveException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleExclusiveException(ExclusiveException ex) {
        apiExceptionHandlerLogger.error("Exclusive error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("03");

        String exceptionExclusiveException = messageSource.getMessage("exception_exclusive_exception", null, Locale.JAPAN);
        errorDetailDto.setMessage(exceptionExclusiveException);

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleDuplicatedUserException(DuplicatedUserException ex) {
        apiExceptionHandlerLogger.error("DuplicatedUser error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("05");

        String exceptionDuplicatedUserException = messageSource.getMessage("exception_duplicated_user_exception", null, Locale.JAPAN);
        errorDetailDto.setMessage(exceptionDuplicatedUserException);

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(DuplicatedCompanyException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleDuplicatedCompanyException(DuplicatedCompanyException ex) {
        apiExceptionHandlerLogger.error("DuplicatedCompany error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("05");

        String exceptionDuplicatedCompanyException = messageSource.getMessage("exception_duplicated_company_exception", null, Locale.JAPAN);
        errorDetailDto.setMessage(exceptionDuplicatedCompanyException);

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }

    @ExceptionHandler(DuplicatedSettlementException.class)
    public ResponseEntity<ResponseBodyDto<Void>> handleDuplicatedSettlementException(DuplicatedSettlementException ex) {
        apiExceptionHandlerLogger.error("DuplicatedSettlement error occurred: {}", ex.getMessage());

        ResponseBodyDto<Void> responseBodyDto = new ResponseBodyDto<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();

        responseBodyDto.setStatusCode(HttpStatus.OK.value());
        errorDetailDto.setCode("05");

        String exceptionDuplicatedSettlementException = messageSource.getMessage("exception_duplicated_settlement_exception", null, Locale.JAPAN);
        errorDetailDto.setMessage(exceptionDuplicatedSettlementException);

        responseBodyDto = CreateResponseFactory.getErrorResponse(responseBodyDto, errorDetailDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseBodyDto);
    }
}
