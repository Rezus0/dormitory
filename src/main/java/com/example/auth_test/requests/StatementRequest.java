package com.example.auth_test.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementRequest {
    @NotBlank
    private String header;
    @NotBlank
    private String description;
    @NotBlank
    private String tag;
    @NotBlank
    private String image;
}
