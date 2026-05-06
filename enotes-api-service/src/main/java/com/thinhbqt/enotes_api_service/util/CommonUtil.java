package com.thinhbqt.enotes_api_service.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.thinhbqt.enotes_api_service.handler.GenericResponse;

public class CommonUtil {
    public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status){
        GenericResponse response = GenericResponse.builder()
        .responseStatus(status)
        .status("success")
        .data(data)
        .build();
        return response.create();
    }
    public static ResponseEntity<?> createBuildResponseMessage(Object data , HttpStatus status, String message){
        GenericResponse response = GenericResponse.builder()
        .responseStatus(status)
        .status("succcess")
        .message(message)
        .data(data)
        .build();
        return response.create();
    }
    public static ResponseEntity<?> createErrorResponse( HttpStatus status){
        GenericResponse response = GenericResponse.builder()
        .responseStatus(status).status("failed")
        .build();
        return response.create();
    }
    public static ResponseEntity<?> createErrorResponseMessage( String message, HttpStatus status){
        GenericResponse response = GenericResponse.builder()
        .responseStatus(status).status("failed")
        .message(message).build();
        return response.create();
    }
}
