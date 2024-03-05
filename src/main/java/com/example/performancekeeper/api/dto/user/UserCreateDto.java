package com.example.performancekeeper.api.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 회원 가입 요청 DTO 클래스
 */
@Getter
public class UserCreateDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordCheck;

    /**
     * UserCreateDto 생성자
     *
     * @param username 아이디
     * @param password 비밀번호
     * @param passwordCheck 비밀번호 확인
     */
    public UserCreateDto(String username, String password, String passwordCheck) {
        this.username = username;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }
}
