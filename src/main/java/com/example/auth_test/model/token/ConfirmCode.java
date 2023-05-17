package com.example.auth_test.model.token;

import com.example.auth_test.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConfirmCode {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private LocalDateTime expiredAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
