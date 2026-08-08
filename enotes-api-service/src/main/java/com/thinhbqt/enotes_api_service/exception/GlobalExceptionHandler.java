package com.thinhbqt.enotes_api_service.exception;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thinhbqt.enotes_api_service.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("GlobalExceptionHandler::handleException ", e.getMessage());

        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(Exception e) {
        log.error("GlobalExceptionHandler::handleResourceNotFoundException ", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(Exception exception) {
        return CommonUtil.createErrorResponseMessage(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldError = ((FieldError) error).getField();
            String messError = error.getDefaultMessage();
            errors.put(fieldError, messError);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException exception) {
        log.error("GlobalExceptionHandler::handleValidationException ", exception.getMessage());
        return CommonUtil.createErrorResponseMessage(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExistedDataException.class)
    public ResponseEntity<?> handleExistedDataException(ExistedDataException e) {
        log.error("GlobalExceptionHandler::handleExistedDataException ", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException exception) {
        log.error("GlobalExceptionHandler::handleFileNotFoundException ", exception.getMessage());
        return CommonUtil.createErrorResponseMessage(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SuccessException.class)
    public ResponseEntity<?> handleSuccessException(Exception e) {
        log.error("GlobalExceptionHandler::handleSuccessException ", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(Exception e) {
        log.error("GlobalExceptionHandler::handleBadCredentialsException ", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<?> handleJwtTokenExpiredException(JwtTokenExpiredException e) {
        log.error("GlobalExceptionHandler::handleJwtTokenExpiredException ", e.getMessage());
        return CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("GlobalExceptionHandler::handleAccessDeniedException ", ex.getMessage());
        return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
