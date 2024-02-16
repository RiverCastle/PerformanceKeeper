package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.dto.user.UserCreateDto;
import com.example.performancekeeper.api.entity.UserEntity;

public interface UserService {
    UserEntity checkUser(Long userId);
    void createUser(UserCreateDto userCreateDto);
}
