package com.example.auth_test.exceptions;

public class StatementException extends RuntimeException {
    public StatementException() {
        super("Statement doesn't exist");
    }
}
