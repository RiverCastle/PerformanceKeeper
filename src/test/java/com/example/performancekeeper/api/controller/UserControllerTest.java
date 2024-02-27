package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.user.UserCreateDto;
import com.example.performancekeeper.api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    @Test
    @DisplayName("정상 회원가입 요청")
    void signUp() {
        UserCreateDto dto = new UserCreateDto("testUser1", "testUserPw", "testUserPw");
        userController.signUp(dto);
    }
}