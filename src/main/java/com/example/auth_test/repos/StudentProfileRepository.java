package com.example.auth_test.repos;

import com.example.auth_test.model.user.StudentProfile;
import com.example.auth_test.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUser(User user);
}
