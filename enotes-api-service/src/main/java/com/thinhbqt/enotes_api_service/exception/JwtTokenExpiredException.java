package com.thinhbqt.enotes_api_service.exception;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(String msg){
        super(msg);
    }
}
