package com.thinhbqt.enotes_api_service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thinhbqt.enotes_api_service.entity.AccountStatus;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.exception.ResourceNotFoundException;
import com.thinhbqt.enotes_api_service.exception.SuccessException;
import com.thinhbqt.enotes_api_service.repository.UserRepository;
import com.thinhbqt.enotes_api_service.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean verifyAccount(Integer uid, String verificationCode) {
        User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Id user not found"));
        AccountStatus status = user.getStatus();

        if (status.getVerificationCode() == null) {
            throw new SuccessException("Account already verified");
        }
        if (status.getVerificationCode().equals(verificationCode)) {
            status.setIsActive(true);
            status.setVerificationCode(null); 
            userRepository.save(user);
            return true;
        }

        return false;
    }

}
