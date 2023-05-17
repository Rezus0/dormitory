package com.example.auth_test.repos;

import com.example.auth_test.model.token.ConfirmCode;
import com.example.auth_test.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmCodeRepository extends JpaRepository<ConfirmCode, Long> {
    Optional<ConfirmCode> findConfirmCodeByCode(String code);
    @Transactional
    void deleteAllByOwner(User owner);
    @Transactional
    void deleteByExpiredAtBefore(LocalDateTime dateTime);
    @Transactional
    void deleteByCode(String code);
}
