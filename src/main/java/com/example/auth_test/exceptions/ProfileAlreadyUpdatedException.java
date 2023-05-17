package com.example.auth_test.exceptions;

public class ProfileAlreadyUpdatedException extends RuntimeException {
    public ProfileAlreadyUpdatedException() {
        super("Profile already updated");
    }
}
