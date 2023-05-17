package com.example.auth_test.services.tokenService;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TokenService {
    String generateToken(UserDetails userDetails, Map<String, ?> extraClaims);
    boolean isTokenValid(String token);
    Claims extractAllClaims(String token);
}
