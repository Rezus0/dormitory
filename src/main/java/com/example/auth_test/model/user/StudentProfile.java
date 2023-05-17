package com.example.auth_test.model.user;

import com.example.auth_test.model.statement.Block;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "student_profile")
public class StudentProfile {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private String course;
    private String institute;
    private String groupName;
    @Enumerated(EnumType.STRING)
    private TrainingForm trainingForm;
    @Enumerated(EnumType.STRING)
    private BaseOfTraining baseOfTraining;
    @JoinColumn(name = "block_id")
    @ManyToOne
    private Block block;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

