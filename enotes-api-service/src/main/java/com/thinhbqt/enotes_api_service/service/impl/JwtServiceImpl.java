package com.thinhbqt.enotes_api_service.service.impl;

import com.thinhbqt.enotes_api_service.entity.User;
import com.thinhbqt.enotes_api_service.exception.JwtTokenExpiredException;
import com.thinhbqt.enotes_api_service.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    private String secretKey;

    public JwtServiceImpl() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getDecryptKey() {
        byte[] keyBytes = Base64.getDecoder().decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(getDecryptKey()).build()
                .parseSignedClaims(token)
                .getPayload();
        }
        catch(ExpiredJwtException e){
            throw new JwtTokenExpiredException("Token is Expired");
        }
        catch(JwtException e){
            throw new JwtTokenExpiredException("Invalid Jwt token");
        } 
        catch (Exception e) {
            throw e;
        }
        
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date()); 
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", user.getRoles());
        claims.put("status", user.getStatus());

        return Jwts.builder().claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 10))
                .and()
                .signWith(getSignInKey()).compact();

    }

}
