package com.thinhbqt.enotes_api_service.service;

import com.thinhbqt.enotes_api_service.dto.LoginRequest;
import com.thinhbqt.enotes_api_service.dto.LoginResponse;
import com.thinhbqt.enotes_api_service.dto.UserRequest;

public interface AuthService {
    Boolean registerUser(UserRequest userDto, String url);

    LoginResponse login(LoginRequest loginRequest);
}
