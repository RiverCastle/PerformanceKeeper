package com.example.performancekeeper.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.ALREADY_REPORTED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    PASSWORD_CHECK_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 ID입니다."),
    LOGIN_FAILED_NOT_FOUND_USER(HttpStatus.NOT_FOUND, "아이디를 확인해주세요."),
    LOGIN_FAILED_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호를 확인해주세요."),
    RELOGIN_NEEDED(UNAUTHORIZED, "보안을 위해 자동 로그아웃 되었습니다. 재로그인을 해주세요."),
    ALREADY_LOGOUT(ALREADY_REPORTED, "로그아웃 되었습니다.");
    private final HttpStatus httpStatus;
    private final String messagge;
}
