package com.example.performancekeeper.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

/**
 * 서버의 상태와 관련된 요청을 처리하는 Controller입니다.
 * 현재 배포된 서버의 상태를 확인하는 용도로 사용합니다.
 * 현재 사용하고 있는 포트, 서버의 이름을 조회하는 요청을 처리합니다.
 * CICD 배포구조를 구축하면서 생성하였습니다.
 */
@RestController
@RequestMapping("/api/server")
public class ServerCheckController {

    @Value("${server.env}")
    private String env;
    @Value("${server.port}")
    private String port;

    @GetMapping
    public ResponseEntity<?> serverCheck() {
        Map<String, String> responseData = new TreeMap<>();
        responseData.put("env", env);
        responseData.put("portNumber", port);

        return ResponseEntity.ok(responseData);
    }

    /**
     * blue/green 중 사용중인 서버명을 반환합니다.
     * @return "blue" or "green"
     */
    @GetMapping("/env")
    public ResponseEntity<?> getEnv() {
        return ResponseEntity.ok(env);
    }
}

