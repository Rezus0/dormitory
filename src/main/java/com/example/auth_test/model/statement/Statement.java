package com.example.auth_test.model.statement;

import com.example.auth_test.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "_statement")
public class Statement {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User creator;
    private String header;
    private String tag;
    private String description;
    private LocalDateTime date;
    private String image;
}
