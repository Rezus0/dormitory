package com.example.auth_test.requests;

import com.example.auth_test.model.statement.Block;
import com.example.auth_test.model.user.BaseOfTraining;
import com.example.auth_test.model.user.TrainingForm;
import lombok.Builder;

@Builder
public record ProfileResponse
        (
                String course,
                String institute,
                String groupName,
                TrainingForm trainingForm,
                BaseOfTraining baseOfTraining,
                String blockNumber
        ) {
}
