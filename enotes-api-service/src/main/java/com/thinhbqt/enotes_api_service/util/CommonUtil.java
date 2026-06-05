package com.thinhbqt.enotes_api_service.util;

import org.apache.commons.io.FilenameUtils;
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
    public static ResponseEntity<?> createBuildResponseMessage( HttpStatus status, String message){
        GenericResponse response = GenericResponse.builder()
        .responseStatus(status)
        .status("succcess")
        .message(message)
        .build();
        return response.create();
    }
    public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status){
        GenericResponse response = GenericResponse.builder()
        .responseStatus(status).status("failed")
        .data(data)
        .build();   
        return response.create();
    }
    public static ResponseEntity<?> createErrorResponseMessage( String message, HttpStatus status){
        GenericResponse response = GenericResponse.builder()
        .responseStatus(status).status("failed")
        .message(message).build();

        return response.create();
    }

    public static String setContentType(String fileName){
        String extension = FilenameUtils.getExtension(fileName);
        switch (extension.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "png":
                return "image/png";
            case "jpg":
                return "iamge/jpg";
            case "jpeg":
                return "image/jpeg";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            
        
            default:
                return "application/octet-stream";
        }
    }
}
