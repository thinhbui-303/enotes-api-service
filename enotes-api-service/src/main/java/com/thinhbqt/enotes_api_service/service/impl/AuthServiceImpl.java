package com.thinhbqt.enotes_api_service.service.impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thinhbqt.enotes_api_service.config.security.CustomUserDetails;
import com.thinhbqt.enotes_api_service.dto.EmailRequest;
import com.thinhbqt.enotes_api_service.dto.LoginRequest;
import com.thinhbqt.enotes_api_service.dto.LoginResponse;
import com.thinhbqt.enotes_api_service.dto.UserRequest;
import com.thinhbqt.enotes_api_service.entity.AccountStatus;
import com.thinhbqt.enotes_api_service.entity.Role;
import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.repository.RoleRepository;
import com.thinhbqt.enotes_api_service.repository.UserRepository;
import com.thinhbqt.enotes_api_service.service.EmailService;
import com.thinhbqt.enotes_api_service.service.JwtService;
import com.thinhbqt.enotes_api_service.service.AuthService;
import com.thinhbqt.enotes_api_service.util.Validation;

@Service
public class AuthServiceImpl implements AuthService {
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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper mapper;

    @Autowired

    private JwtService jwtService;
    @Override
    public Boolean registerUser(UserRequest userDto, String url) {

        userValidation.validateUser(userDto);

        User user = modelMapper.map(userDto, User.class);

        setRole(userDto, user);
        AccountStatus status = AccountStatus.builder().isActive(false)
                .verificationCode(UUID.randomUUID().toString()).build();
        user.setStatus(status);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        if (savedUser != null) {
            sendRegisterConfirmationEmail(savedUser, url);
        }
        return savedUser != null;
    }

    private void setRole(UserRequest userDto, User user) {
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
        msg = msg.replace("[[url]]", url + "/api/v1/home/verify?uid=" + saveUser.getId() + "&&code="
                + saveUser.getStatus().getVerificationCode());

        EmailRequest emailRequest = EmailRequest.builder()
                .to(saveUser.getEmail())
                .title("Account Creation Confirmation")
                .subject("Account Created Successfully")
                .message(msg)
                .build();

        emailService.sendEmail(emailRequest);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
            User user = customUserDetails.getUser();

            UserRequest userDto = mapper.map(user, UserRequest.class);

            String token = jwtService.generateToken(user);
            LoginResponse loginResponse = LoginResponse.builder()
            .user(userDto).token(token).build();

            return loginResponse;
        
    }

}
