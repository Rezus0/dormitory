package com.example.auth_test.services.tokenService;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.function.Function;

@Service
public abstract class AbstractTokenService implements TokenService {
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }
    public String extractEmail(String token) {
        return extractSingleClaim(token, Claims::getSubject);
    }
    public <T> T extractSingleClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
}

