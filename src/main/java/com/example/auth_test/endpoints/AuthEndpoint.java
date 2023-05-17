package com.example.auth_test.endpoints;

import com.example.auth_test.requests.AuthenticationRequest;
import com.example.auth_test.requests.AuthenticationResponse;
import com.example.auth_test.requests.RegisterRequest;
import com.example.auth_test.services.AuthService;
import com.example.auth_test.services.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthEndpoint {
    private final AuthService authService;
    private final CookieService cookieService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse servletResponse) {
        AuthenticationResponse response = authService.refreshTokens(request);
        cookieService.configureCookies(response)
                .forEach(servletResponse::addCookie);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody @Valid AuthenticationRequest request,
            HttpServletResponse servletResponse
            ) {
        if (authService.isUserAccountConfirmed(request)) {
            AuthenticationResponse response = authService.authenticate(request);
            cookieService.configureCookies(response)
                    .forEach(servletResponse::addCookie);
            return ResponseEntity.ok(response);
        }
        String response = authService.sendCode(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("code") String code) {
        String response = authService.confirm(code);
        return ResponseEntity.ok(response);
    }

}
