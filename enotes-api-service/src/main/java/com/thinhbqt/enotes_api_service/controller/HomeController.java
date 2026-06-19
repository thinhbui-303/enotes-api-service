package com.thinhbqt.enotes_api_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.service.HomeService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam("uid") Integer uid, 
                                               @RequestParam("code") String code) {
        Boolean isVerified = homeService.verifyAccount(uid, code);
        if (isVerified) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Account verification success");
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }
}