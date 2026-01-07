package com.gateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "healthy");

        try {
            jdbcTemplate.execute("SELECT 1");
            response.put("database", "connected");
        } catch (Exception e) {
            response.put("database", "disconnected");
        }

        response.put("timestamp", Instant.now().toString());
        return response;
    }
}
