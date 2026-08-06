package com.thinhbqt.enotes_api_service.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinhbqt.enotes_api_service.dto.LoginRequest;
import com.thinhbqt.enotes_api_service.dto.UserRequest;
import com.thinhbqt.enotes_api_service.util.ApiCommonResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Authentication", description = "You can sign in or sign up")
@ApiCommonResponses
@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {
    @Operation(summary = "Register account", description = "Click on it if you dont have any account to login")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request);

    @Operation(summary = "Login account", description = "Click on it if you already had account to login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest);
}
