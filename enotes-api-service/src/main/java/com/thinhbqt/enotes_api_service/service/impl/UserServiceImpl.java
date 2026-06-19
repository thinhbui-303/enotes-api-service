package com.thinhbqt.enotes_api_service.service.impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinhbqt.enotes_api_service.dto.EmailRequest;
import com.thinhbqt.enotes_api_service.dto.UserDto;
import com.thinhbqt.enotes_api_service.entity.AccountStatus;
import com.thinhbqt.enotes_api_service.entity.Role;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.repository.RoleRepository;
import com.thinhbqt.enotes_api_service.repository.UserRepository;
import com.thinhbqt.enotes_api_service.service.EmailService;
import com.thinhbqt.enotes_api_service.service.UserService;
import com.thinhbqt.enotes_api_service.util.Validation;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Validation userValidation;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;

    @Override
    public Boolean registerUser(UserDto userDto, String url) {
        
        userValidation.validateUser(userDto);

        User user = modelMapper.map(userDto, User.class);

       
        setRole(userDto, user); 
        AccountStatus status = AccountStatus.builder().isActive(false)
        .verificationCode(UUID.randomUUID().toString()).build();
        user.setStatus(status);

        User savedUser = userRepository.save(user);
        if (savedUser != null) {
        sendRegisterConfirmationEmail(savedUser, url);
    }
        return savedUser != null;
    }
    private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepository.findAllById(reqRoleId);
		user.setRoles(roles);
	}
    private void sendRegisterConfirmationEmail(User saveUser, String url) {
    String msg = "Hi <b>" + saveUser.getFirstName() + "</b>,<br><br>"
            + "Your account registered successfully.<br>"
            + "Click the below link to verify and active your account:<br>"
            + "<a href='[[url]]'>Click Here</a><br><br>"
            + "Thanks,<br>Enotes Team.";
    msg = msg.replace("[[url]]",url + "/api/v1/home/verify?uid=" + saveUser.getId() + "&&code="
				+ saveUser.getStatus().getVerificationCode());
    
    EmailRequest emailRequest = EmailRequest.builder()
            .to(saveUser.getEmail())
            .title("Account Creation Confirmation")
            .subject("Account Created Successfully")
            .message(msg)
            .build();

    
    emailService.sendEmail(emailRequest);
}
    
}
