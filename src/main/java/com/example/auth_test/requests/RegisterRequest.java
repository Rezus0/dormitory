package com.example.auth_test.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.JsonAdapter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(max = 20)
    @NotBlank
    private String name;
    @Size(max = 30)
    @NotBlank
    private String surname;
    @Size(max = 30)
    @NotBlank
    private String patronymic;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
    @Email
    @Size(max = 40)
    @NotBlank
    private String email;
    @Size(min = 6, max = 50)
    @NotBlank
    private String password;
}
