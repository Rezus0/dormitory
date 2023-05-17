package com.example.auth_test.model.statement;

import com.example.auth_test.model.user.StudentProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Entity
@Data
@Builder
@Table(name = "block")
public class Block {
    @Id
    @JsonIgnore
    @GeneratedValue
    private Long id;
    private String number;
    @Enumerated(EnumType.STRING)
    private Mark mark = Mark.NA;
    @OneToMany(mappedBy = "block")
    @Builder.ObtainVia(field = "students")
    private List<StudentProfile> students = new ArrayList<>();

    public Block(String number) {
        this.number = number;
    }

    public Block() {

    }
}
