package com.thinhbqt.enotes_api_service.exception;

public class ExistedDataException extends RuntimeException {
    ExistedDataException(String msg){
        super(msg);
    }
}
