package com.example.demo.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleRunTimeException(RuntimeException e) {
        Map<String,String> map = new HashMap<>();
        map.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
