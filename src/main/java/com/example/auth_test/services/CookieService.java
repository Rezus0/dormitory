package com.example.auth_test.services;

import com.example.auth_test.requests.AuthenticationResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final Integer REFRESH_COOKIE_EXPIRY_SECS = 2678400; // 31 days
    private final Integer ACCESS_COOKIE_EXPIRY_SECS = 86400; // 24 hours
    public List<Cookie> configureCookies(AuthenticationResponse response) {
        Cookie refreshTokenCookie = new Cookie("REFRESH-TOKEN", response.refreshToken());
        Cookie acessTokenCookie = configureAccessCookie(response.accessToken());
        refreshTokenCookie.setMaxAge(REFRESH_COOKIE_EXPIRY_SECS);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        return List.of(acessTokenCookie, refreshTokenCookie);
    }

    public String extractToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("ACCESS-TOKEN"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow();
    }

    public Cookie configureAccessCookie(String accessToken) {
        Cookie acessTokenCookie = new Cookie("ACCESS-TOKEN", accessToken);
        acessTokenCookie.setMaxAge(ACCESS_COOKIE_EXPIRY_SECS);
        acessTokenCookie.setHttpOnly(true);
        acessTokenCookie.setPath("/");
        return acessTokenCookie;
    }

    public Cookie clearResfreshCookie() {
        Cookie cookie = new Cookie("REFRESH-TOKEN", "");
        cookie.setMaxAge(0);
        return cookie;
    }
}
