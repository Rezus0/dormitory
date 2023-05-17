package com.example.auth_test.repos;

import com.example.auth_test.model.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query(
            "update User u set u.activated = true where u.email = :email"
    )
    void updateConfirmedForEmail(@Param("email") String email);
}
