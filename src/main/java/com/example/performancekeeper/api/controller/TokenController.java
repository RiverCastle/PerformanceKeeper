package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.token.AccessTokenDto;
import com.example.performancekeeper.api.dto.token.LoginRequestDto;
import com.example.performancekeeper.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * JWT와 관련된 요청을 처리하는 Controller입니다.
 * 토큰 생성 및 삭제 요청을 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {
    private final TokenService tokenService;
    @PostMapping
    public AccessTokenDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return new AccessTokenDto(tokenService.issueAccessToken(loginRequestDto));
    }
    @DeleteMapping
    public void logout(Authentication authentication,
                       @RequestHeader("Authorization") String bearerToken) {
        String accessToken = bearerToken.split(" ")[1];
        tokenService.deleteToken(accessToken);
    }
}
