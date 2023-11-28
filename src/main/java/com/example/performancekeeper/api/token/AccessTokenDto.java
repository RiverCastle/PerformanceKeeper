package com.example.performancekeeper.api.token;

import lombok.Data;

@Data
public class AccessTokenDto {
    private String token;

    public AccessTokenDto(String token) {
        this.token = token;
    }
}
