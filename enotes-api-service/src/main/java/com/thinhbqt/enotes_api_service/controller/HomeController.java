package com.thinhbqt.enotes_api_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.PasswordResetRequest;
import com.thinhbqt.enotes_api_service.endpoint.HomeEndpoint;
import com.thinhbqt.enotes_api_service.service.HomeService;
import com.thinhbqt.enotes_api_service.service.UserService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController implements HomeEndpoint {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> verifyUserAccount(Integer uid,
            String code) {
        Boolean isVerified = homeService.verifyAccount(uid, code);
        if (isVerified) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Account verification success");
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> sendEmailForPasswordReset(String email, HttpServletRequest request) throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Email Send Success !! Check Email Reset Password");
    }

    @Override
    public ResponseEntity<?> verifyPasswordResetLink(Integer uid, String code) throws Exception {
        userService.verifyPasswordResetLink(uid, code);
        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "verification success");
    }

    @Override
    public ResponseEntity<?> resetPassword(PasswordResetRequest pswdResetRequest) throws Exception {
        userService.resetPassword(pswdResetRequest);
        return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Password reset succes");
    }
}