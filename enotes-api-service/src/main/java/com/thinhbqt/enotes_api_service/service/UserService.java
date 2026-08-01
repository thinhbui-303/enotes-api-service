package com.thinhbqt.enotes_api_service.service;

import com.thinhbqt.enotes_api_service.dto.PasswordChangeRequest;
import com.thinhbqt.enotes_api_service.dto.PasswordResetRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    public void changePassword(PasswordChangeRequest request);
    
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

	public void verifyPasswordResetLink(Integer uid, String code) throws Exception;

	public void resetPassword(PasswordResetRequest pswdResetRequest) throws Exception;
}
