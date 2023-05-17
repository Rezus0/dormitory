package com.example.auth_test.config;

import com.example.auth_test.services.CookieService;
import com.example.auth_test.services.tokenService.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final RequestMatcher pathMatcher = new AntPathRequestMatcher("/api/v1/auth/**");
    private final UserDetailsService userDetailsService;
    private final AccessTokenService accessTokenService;
    private final CookieService cookieService;

    @Override
//    @TrackTime
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (pathMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        Optional<Cookie> accessTokenCookie = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("ACCESS-TOKEN"))
                        .findFirst());
        if (accessTokenCookie.isPresent()) {
            final String accessToken = accessTokenCookie.get().getValue();
            if (accessTokenService.isTokenValid(accessToken)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService
                        .loadUserByUsername(accessTokenService.extractEmail(accessToken));
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } else {
            Cookie cookie = cookieService.clearResfreshCookie();
            response.addCookie(cookie);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        filterChain.doFilter(request,response);
    }
}

