package com.thinhbqt.enotes_api_service.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinhbqt.enotes_api_service.dto.LoginRequest;
import com.thinhbqt.enotes_api_service.dto.PasswordResetRequest;
import com.thinhbqt.enotes_api_service.dto.UserRequest;
import com.thinhbqt.enotes_api_service.util.ApiCommonResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Home")
@ApiCommonResponses
@RequestMapping("/api/v1/home")
public interface HomeEndpoint {
    @Operation(summary = "verify account", tags = {
            "Home" }, description = "User Account verification after register account")
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam("uid") Integer uid,
            @RequestParam("code") String code);

    @Operation(summary = "Send Email for Password Reset", tags = {
            "Home" }, description = "User Can send Email for password reset")
    @GetMapping("/send-email-reset")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request)
            throws Exception;

    @Operation(summary = "Verification password link", tags = {
            "Home" }, description = "User verification password link")
    @GetMapping("/verify-pwd-link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code)
            throws Exception;

    @Operation(summary = "Reset Password", tags = { "Home" }, description = "User Can changes Password")
    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest pswdResetRequest) throws Exception;
}
