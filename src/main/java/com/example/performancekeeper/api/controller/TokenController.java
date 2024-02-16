package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.token.AccessTokenDto;
import com.example.performancekeeper.api.dto.token.LoginRequestDto;
import com.example.performancekeeper.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
