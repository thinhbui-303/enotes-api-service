package com.thinhbqt.enotes_api_service.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.util.CommonUtil;

public class AuditAwareConfig implements AuditorAware<Integer>{
    @Override
    public Optional<Integer> getCurrentAuditor(){
        User loggedInUser = CommonUtil.getLoggedInUser();
        return Optional.of(loggedInUser.getId());
    }
}
