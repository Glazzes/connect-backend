package com.connect.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisMessingController {
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Void> insertSomething() throws IOException {
        return ResponseEntity.status(201).build();
    }

}