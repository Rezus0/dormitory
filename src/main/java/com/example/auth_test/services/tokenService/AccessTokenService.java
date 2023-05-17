package com.example.auth_test.services.tokenService;

import com.example.auth_test.repos.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccessTokenService extends AbstractTokenService {

    private final UserRepository userRepository;

    private static final String ACCESS_TOKEN_SECRET = System.getenv("ACCESS_TOKEN_SECRET");
    private final Long EXPIRY_ACCESS_TOKEN_MILLIS = 86400000L;

    @Override
    public String generateToken(UserDetails userDetails, Map<String, ?> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_ACCESS_TOKEN_MILLIS))
                .signWith(getSingingKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token) &&
                userRepository.findUserByEmail(extractEmail(token)).isPresent();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSingingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractSingleClaim(token, Claims::getExpiration)
                .before(new Date(System.currentTimeMillis()));
    }

    private Key getSingingKey() {
        byte[] key = Decoders.BASE64.decode(AccessTokenService.ACCESS_TOKEN_SECRET);
        return Keys.hmacShaKeyFor(key);
    }

}
