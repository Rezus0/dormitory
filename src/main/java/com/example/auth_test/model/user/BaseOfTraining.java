package com.example.auth_test.model.user;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ser.Serializers;

public enum BaseOfTraining {
    BUDGET("budget"),
    COMMERCIAL("commercial"),
    TARGETED("targeted");
    private final String name;
    BaseOfTraining(String name) {
        this.name = name;
    }
    @JsonValue
    public String getName() {
        return name;
    }

}
