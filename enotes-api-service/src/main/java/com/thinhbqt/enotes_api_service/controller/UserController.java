package com.thinhbqt.enotes_api_service.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.PasswordChangeRequest;
import com.thinhbqt.enotes_api_service.dto.UserResponse;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.service.UserService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {

        User loggedInUser = CommonUtil.getLoggedInUser();

        UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);

        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }

    @PostMapping("/change-pwd")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        userService.changePassword(request);
        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Change password successfully!");

    }
    
}
