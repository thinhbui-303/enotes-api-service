package com.thinhbqt.enotes_api_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.LoginRequest;
import com.thinhbqt.enotes_api_service.dto.LoginResponse;
import com.thinhbqt.enotes_api_service.dto.UserRequest;
import com.thinhbqt.enotes_api_service.endpoint.AuthEndpoint;
import com.thinhbqt.enotes_api_service.service.AuthService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthEndpoint {
    private final AuthService userService;

    @Override
    public ResponseEntity<?> registerUser(UserRequest userDto, HttpServletRequest request) {
        String url = CommonUtil.getSiteURL(request);
        Boolean isRegistered = userService.registerUser(userDto, url);
        if (isRegistered) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Register successful");
        } else {
            return CommonUtil.createErrorResponseMessage("Register failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        if (ObjectUtils.isEmpty(loginResponse)) {
            return CommonUtil.createErrorResponseMessage("Invalid credentials!", HttpStatus.BAD_GATEWAY);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }
}
