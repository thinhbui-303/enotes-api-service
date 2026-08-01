package com.thinhbqt.enotes_api_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thinhbqt.enotes_api_service.dto.PasswordResetRequest;
import com.thinhbqt.enotes_api_service.service.HomeService;
import com.thinhbqt.enotes_api_service.service.UserService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam("uid") Integer uid, 
                                               @RequestParam("code") String code) {
        Boolean isVerified = homeService.verifyAccount(uid, code);
        if (isVerified) {
            return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Account verification success");
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception {
		userService.sendEmailPasswordReset(email, request);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK,"Email Send Success !! Check Email Reset Password");
	}

	@GetMapping("/verify-pwd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid,@RequestParam String code) throws Exception {
		userService.verifyPasswordResetLink(uid, code);
		return CommonUtil.createBuildResponseMessage( HttpStatus.OK,"verification success");
	}

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest pswdResetRequest) throws Exception {
		userService.resetPassword(pswdResetRequest);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK,"Password reset succes");
	}
}