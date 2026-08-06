package com.thinhbqt.enotes_api_service.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private final UserRepository userRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String email){
       User user =  userRepository.findByEmail(email);
       if(user == null){
        throw new UsernameNotFoundException("User name not found!");
       }
       return new CustomUserDetails(user);
    }
}
