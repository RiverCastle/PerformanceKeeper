package com.example.performancekeeper.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCreateDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;

    public UserCreateDto(String username, String password, String passwordCheck) {
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }
}
