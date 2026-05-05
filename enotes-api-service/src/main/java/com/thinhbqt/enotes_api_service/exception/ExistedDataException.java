package com.thinhbqt.enotes_api_service.exception;

public class ExistedDataException extends RuntimeException {
    public ExistedDataException(String msg){
        super(msg);
    }
}
