package com.example.performancekeeper.api.users;

public interface UserService {
    UserEntity checkUser(Long userId);
    void createUser(UserCreateDto userCreateDto);
}
