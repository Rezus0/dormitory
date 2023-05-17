package com.example.auth_test.exceptions;

public class CodeExpiredException extends RuntimeException {
    public CodeExpiredException() {
        super("Confirm code has expired or has been already used");
    }
}
