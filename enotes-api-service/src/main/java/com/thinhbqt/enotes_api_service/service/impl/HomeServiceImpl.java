package com.thinhbqt.enotes_api_service.service.impl;

import org.springframework.stereotype.Component;

import com.thinhbqt.enotes_api_service.entity.AccountStatus;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.exception.SuccessException;
import com.thinhbqt.enotes_api_service.repository.UserRepository;
import com.thinhbqt.enotes_api_service.service.HomeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final UserRepository userRepository;

    @Override
    public Boolean verifyAccount(Integer uid, String verificationCode) {
        log.info("HomeServiceImpl: Execution Start: verifyAccount method! with id: ", uid);

        User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Id user not found"));
        AccountStatus status = user.getStatus();

        if (status.getVerificationCode() == null) {
            throw new SuccessException("Account already verified");
        }
        if (status.getVerificationCode().equals(verificationCode)) {
            status.setIsActive(true);
            status.setVerificationCode(null);
            userRepository.save(user);
            log.info("Execution success: verifyAccount done!");
            return true;
        }
        log.info("Execution fail: verifyAccount fail!");
        log.info("Execution end: verifyAccount method!");
        return false;
    }

}
