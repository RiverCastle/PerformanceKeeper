package com.example.performancekeeper.api.token;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String username;
    private String password;

    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
