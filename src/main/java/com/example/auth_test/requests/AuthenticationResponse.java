package com.example.auth_test.requests;

import lombok.Builder;

@Builder
public record AuthenticationResponse (String accessToken, String refreshToken) {}
