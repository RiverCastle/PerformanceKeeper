package com.example.performancekeeper.api.users;

import lombok.Getter;

@Getter
public class UserCreateDto {
    private String username;
    private String password;
    private String passwordCheck;
}
