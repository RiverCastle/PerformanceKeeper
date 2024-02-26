package com.example.performancekeeper.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/server")
public class ServerCheckController {

    @Value("${server.env}")
    private String env;
    @Value("${server.port}")
    private String port;
    @Value("${server.serverAddress}")
    private String serverAddress;

    @GetMapping
    public ResponseEntity<?> serverCheck() {
        Map<String, String> responseData = new TreeMap<>();
        responseData.put("env", env);
        responseData.put("portNumber", port);
        responseData.put("serverAddress", serverAddress);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv() {
        return ResponseEntity.ok(env);
    }
}

