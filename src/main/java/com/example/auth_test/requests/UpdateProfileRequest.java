package com.example.auth_test.requests;

import com.example.auth_test.model.statement.Block;
import com.example.auth_test.model.user.BaseOfTraining;
import com.example.auth_test.model.user.TrainingForm;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    @NotBlank
    private String course;
    @NotBlank
    private String institute;
    @NotBlank
    private String groupName;
    private TrainingForm trainingForm;
    private BaseOfTraining baseOfTraining;
    @NotBlank
    private String blockNumber;
}
