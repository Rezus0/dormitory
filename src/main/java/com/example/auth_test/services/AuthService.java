package com.example.auth_test.services;

import com.example.auth_test.email.EmailService;
import com.example.auth_test.exceptions.SessionExpiredException;
import com.example.auth_test.exceptions.UserAlreadyExistsException;
import com.example.auth_test.requests.AuthenticationRequest;
import com.example.auth_test.requests.AuthenticationResponse;
import com.example.auth_test.requests.RegisterRequest;
import com.example.auth_test.model.user.Role;
import com.example.auth_test.model.user.User;
import com.example.auth_test.services.tokenService.AccessTokenService;
import com.example.auth_test.services.tokenService.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final ConfirmCodeService confirmCodeService;

    public String register(RegisterRequest request) {
        if (userService.isUserExists(request.getEmail()))
            throw new UserAlreadyExistsException();
        User user = User.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .birthday(request.getBirthday())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getEmail().startsWith("rere") ?
                        List.of(Role.EMPLOYEE, Role.WORKER, Role.ADMIN) : List.of(Role.STUDENT))
                .build();
        userService.save(user);
        return "User has been successfully registered."
                + "\n" +
                sendCode(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userService.getUser(request.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        return AuthenticationResponse.builder()
                .accessToken(accessTokenService.generateToken(user))
                .refreshToken(refreshTokenService.generateToken(user))
                .build();
    }

    public String confirm(String confirmCode) {
        User user = confirmCodeService.getCodeOwner(confirmCode);
        userService.confirmAccount(user.getEmail());
        confirmCodeService.deleteCode(confirmCode);
        return "Your account has been successfully confirmed."
                + "\n" +
                "Login using your email and password.";
    }

    public String sendCode(String email) {
        User user = userService.getUser(email);
        confirmCodeService.deleteAllTokensByUser(user);
        final String code = confirmCodeService.generateConfirmCode(user);
        final String message = emailService.buildEmail(user.getName(), code);
        emailService.send(user.getEmail(), message);
        return "Please verify your account via link which has been sent on your email address.";
    }

    private String sendCode(User user) {
        final String code = confirmCodeService.generateConfirmCode(user);
        final String message = emailService.buildEmail(user.getName(), code);
        emailService.send(user.getEmail(), message);
        return "Please verify your account via link which has been sent on your email address.";
    }

    public boolean isUserAccountConfirmed(AuthenticationRequest request) {
        return userService.getUser(request.getEmail()).isEnabled();
    }

    public AuthenticationResponse refreshTokens(HttpServletRequest request) {
        Optional<Cookie> refreshCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("REFRESH-TOKEN"))
                .findFirst();
        if (refreshCookie.isPresent()) {
            String oldRefreshToken = refreshCookie.get().getValue();
            if (refreshTokenService.isTokenValid(oldRefreshToken)) {
                refreshTokenService.deleteToken(oldRefreshToken);
                String email = refreshTokenService.extractEmail(oldRefreshToken);
                User user = userService.getUser(email);
                return AuthenticationResponse.builder()
                        .accessToken(accessTokenService.generateToken(user))
                        .refreshToken(refreshTokenService.generateToken(user))
                        .build();
            }
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        throw new SessionExpiredException();
    }
}
