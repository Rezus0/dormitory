package com.example.auth_test.exceptions;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() {
        super("Your session has expired");
    }
}
