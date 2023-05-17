package com.example.auth_test.email;

public interface EmailSender {
    void send(String to, String message);
}
