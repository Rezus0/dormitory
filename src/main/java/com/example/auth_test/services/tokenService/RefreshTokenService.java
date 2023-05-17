package com.example.auth_test.services.tokenService;

import com.example.auth_test.repos.RefreshTokenRepository;
import com.example.auth_test.model.token.RefreshToken;
import com.example.auth_test.repos.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService extends AbstractTokenService {

    private static final String REFRESH_TOKEN_SECRET = System.getenv("REFRESH_TOKEN_SECRET");
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public String generateToken(UserDetails userDetails, Map<String, ?> extraClaims) {
        RefreshToken refreshTokenEntity = new RefreshToken();
        String tokenBody = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(refreshTokenEntity.getExpiryTimeMillis()))
                .signWith(getSingingKey(), SignatureAlgorithm.HS512)
                .compact();
        refreshTokenEntity.setBody(tokenBody);
        refreshTokenRepository.save(refreshTokenEntity);
        return tokenBody;
    }

    public void deleteExpiredTokens() {
        Long currentMillis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        refreshTokenRepository.deleteAllByExpiryTimeMillisLessThan(currentMillis);
    }

    @Transactional
    public void deleteToken(String token) {
        refreshTokenRepository.removeRefreshTokenByBody(token);
    }

    private boolean isTokenInDB(String token) {
        return refreshTokenRepository.findRefreshTokenByBody(token).isPresent();
    }

    @Override
    public boolean isTokenValid(String token) {
        return !isTokenExpired(token)
                &&
                userRepository.findUserByEmail(extractEmail(token)).isPresent()
                &&
                isTokenInDB(token);
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
        byte[] key = Decoders.BASE64.decode(RefreshTokenService.REFRESH_TOKEN_SECRET);
        return Keys.hmacShaKeyFor(key);
    }

}
