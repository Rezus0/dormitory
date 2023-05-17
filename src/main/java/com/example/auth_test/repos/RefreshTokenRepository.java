package com.example.auth_test.repos;

import com.example.auth_test.model.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByBody(String body);
    void deleteAllByExpiryTimeMillisLessThan(Long currentMillis);
    void removeRefreshTokenByBody(String body);
}
