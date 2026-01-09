package com.gateway.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("code", "NOT_FOUND_ERROR");
        error.put("description", ex.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("error", error);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
