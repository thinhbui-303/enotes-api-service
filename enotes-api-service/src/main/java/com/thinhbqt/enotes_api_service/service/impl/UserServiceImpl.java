package com.thinhbqt.enotes_api_service.service.impl;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.thinhbqt.enotes_api_service.dto.EmailRequest;
import com.thinhbqt.enotes_api_service.dto.PasswordChangeRequest;
import com.thinhbqt.enotes_api_service.dto.PasswordResetRequest;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.repository.UserRepository;
import com.thinhbqt.enotes_api_service.service.EmailService;
import com.thinhbqt.enotes_api_service.service.UserService;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final EmailService emailService;

    @Override
    public void changePassword(PasswordChangeRequest request) {
        // log.info("UserServiceImpl : Execution Start: changePassword method with email: {}", CommonUtil.getLoggedInUser().getEmail());

        User loggedInUser = CommonUtil.getLoggedInUser();

        boolean isMatched = passwordEncoder.matches(request.getOldPassword(), loggedInUser.getPassword());

        if (!isMatched) {
            throw new IllegalArgumentException("Old password is incorrect!");
        }

        loggedInUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(loggedInUser);
        // log.info("Execution end: changePassword method done ");

    }

    @Override
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception {
        // log.info("UserServiceImpl : Execution Start: sendEmailPasswordReset method with email: {}", email);
        User user = userRepository.findByEmail(email);
        if (ObjectUtils.isEmpty(user)) {
            throw new ResourceNotFoundException("invalid Email");
        }

        // Generate unique password reset token
        String passwordResetToken = UUID.randomUUID().toString();
        user.getStatus().setPasswordResetToken(passwordResetToken);
        User updateUser = userRepository.save(user);

        String url = CommonUtil.getSiteURL(request);
        sendEmailRequest(updateUser, url);
        // log.info("Execution end: sendEmailPasswordReset method done ");
    }

    private void sendEmailRequest(User user, String url) throws Exception {

        String message = "Hi <b>[[username]]</b> "
                + "<br><p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=[[url]]>Change my password</a></p>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p><br>"
                + "Thanks,<br>Enotes.com";

        message = message.replace("[[username]]", user.getLastName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify-pwd-link?uid=" + user.getId() + "&&code="
                + user.getStatus().getPasswordResetToken());

        EmailRequest emailRequest = EmailRequest.builder().to(user.getEmail())
                .title("Password Reset").subject("Password Reset link").message(message).build();

        // send password reset email to user
        emailService.sendEmail(emailRequest);
    }

    @Override
    public void verifyPasswordResetLink(Integer uid, String code) throws Exception {
        User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("invalid user"));
        verifyPasswordResetToken(user.getStatus().getPasswordResetToken(), code);

    }

    private void verifyPasswordResetToken(String existToken, String reqToken) {

        // request token not null
        if (StringUtils.hasText(reqToken)) {
            // password already reset
            if (!StringUtils.hasText(existToken)) {
                throw new IllegalArgumentException("Already Password reset");
            }
            // user req token changes
            if (!existToken.equals(reqToken)) {
                throw new IllegalArgumentException("invalid url");
            }
        } else {
            throw new IllegalArgumentException("invalid token");
        }
    }

    @Override
    public void resetPassword(PasswordResetRequest pswdResetRequest) throws Exception {
        // log.info("UserServiceImpl : Execution Start: resetPassword method with user id: ", pswdResetRequest.getUid());
        User user = userRepository.findById(pswdResetRequest.getUid())
                .orElseThrow(() -> new ResourceNotFoundException("invalid user"));
        String encodePassword = passwordEncoder.encode(pswdResetRequest.getNewPassword());
        user.setPassword(encodePassword);
        user.getStatus().setPasswordResetToken(null);
        userRepository.save(user);
        // log.info("Execution end: resetPassword method done ");

    }
}
