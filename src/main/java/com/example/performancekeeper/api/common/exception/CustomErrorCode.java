package com.example.performancekeeper.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    PASSWORD_CHECK_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."), USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 ID입니다.");
    private final HttpStatus httpStatus;
    private final String messagge;
}
