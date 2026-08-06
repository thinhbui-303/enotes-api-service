package com.thinhbqt.enotes_api_service.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.PasswordChangeRequest;
import com.thinhbqt.enotes_api_service.dto.UserResponse;
import com.thinhbqt.enotes_api_service.endpoint.UserEndpoint;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.service.UserService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserEndpoint {
    private final ModelMapper mapper;

    private final UserService userService;

    @Override
    public ResponseEntity<?> getProfile() {

        User loggedInUser = CommonUtil.getLoggedInUser();

        UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);

        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(PasswordChangeRequest request) {
        userService.changePassword(request);
        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Change password successfully!");

    }

}
