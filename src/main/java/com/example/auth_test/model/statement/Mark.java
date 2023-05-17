package com.example.auth_test.model.statement;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Mark {
    EXCELLENT(5),
    GOOD(4),
    OK(3),
    BAD(2),
    AWFUL(1),
    NA(0);
    private final int value;
    Mark(int value) {
        this.value = value;
    }
    @JsonValue
    public int getValue() {
        return value;
    }

}
