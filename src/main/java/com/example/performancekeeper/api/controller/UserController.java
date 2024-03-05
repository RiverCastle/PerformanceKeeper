package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.user.UserCreateDto;
import com.example.performancekeeper.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 관련 Controller입니다.
 * 회원 가입 요청을 처리합니다.
 *
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 요청 처리 메서드입니다.
     *
     * @param userCreateDto
     */
    @PostMapping
    public void signUp(@RequestBody @Valid UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto);
    }
}
