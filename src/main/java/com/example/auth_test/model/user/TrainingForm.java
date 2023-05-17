package com.example.auth_test.model.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TrainingForm {
    FULL_TIME("full time"),
    FULL_PART_TIME("full-part time"),
    PART_TIME("part time");
    private final String name;
    TrainingForm(String name) {
        this.name = name;
    }
    @JsonValue
    public String getName() {
        return name;
    }

}
