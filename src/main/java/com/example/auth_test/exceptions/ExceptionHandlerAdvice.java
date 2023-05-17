package com.example.auth_test.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public ResponseEntity<String> handleException(RuntimeException e) {
        log.warn(e.getMessage());
        Arrays.stream(e.getStackTrace())
                .forEach(er -> log.warn(er.toString()));
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
