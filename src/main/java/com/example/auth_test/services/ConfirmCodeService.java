package com.example.auth_test.services;

import com.example.auth_test.exceptions.CodeExpiredException;
import com.example.auth_test.model.token.ConfirmCode;
import com.example.auth_test.model.user.User;
import com.example.auth_test.repos.ConfirmCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmCodeService {
    private final ConfirmCodeRepository confirmCodeRepository;

    public String generateConfirmCode(User user) {
        ConfirmCode confirmCode = ConfirmCode.builder()
                .code(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusSeconds(900))
                .owner(user)
                .build();
        confirmCodeRepository.save(confirmCode);
        return confirmCode.getCode();
    }

    public void deleteAllTokensByUser(User user) {
        confirmCodeRepository.deleteAllByOwner(user);
    }

    public void deleteCode(String code) {
        confirmCodeRepository.deleteByCode(code);
    }

    public void deleteExpiredCodes() {
        confirmCodeRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }

    public User getCodeOwner(String code) {
        ConfirmCode confirmCode = confirmCodeRepository.findConfirmCodeByCode(code)
                .orElseThrow(CodeExpiredException::new);
        if (confirmCode.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new CodeExpiredException();
        return confirmCode.getOwner();
    }

}
