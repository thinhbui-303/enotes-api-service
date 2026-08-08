package com.thinhbqt.enotes_api_service.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.thinhbqt.enotes_api_service.entity.User;

public interface JwtService {
    public String  generateToken(User user);
    public String extractUsername(String token);
    public boolean validateToken(String token, UserDetails userDetails);

}
