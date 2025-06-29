package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/logs")
    public List<Map<String, Object>> getLogs() {
        return jdbcTemplate.queryForList("SELECT * FROM test_log LIMIT 10");
    }
}
