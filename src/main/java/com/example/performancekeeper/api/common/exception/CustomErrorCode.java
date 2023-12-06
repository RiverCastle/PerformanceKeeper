package com.example.performancekeeper.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    PASSWORD_CHECK_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    USERNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 ID입니다."),
    LOGIN_FAILED_NOT_FOUND_USER(HttpStatus.NOT_FOUND, "아이디를 확인해주세요."),
    LOGIN_FAILED_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호를 확인해주세요."),
    RELOGIN_NEEDED(UNAUTHORIZED, "보안을 위해 자동 로그아웃 되었습니다. 재로그인을 해주세요."),
    ALREADY_LOGOUT(ALREADY_REPORTED, "로그아웃 되었습니다."),
    NOT_FOUND_USER(NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_FOUND_COURSE(NOT_FOUND, "존재하지 않는 강의실입니다."),
    JOINCODE_MISMATCH(BAD_REQUEST, "가입코드를 확인해주세요."),
    NOT_MANAGER(UNAUTHORIZED, "강의실 매니저만 사용할 수 있는 기능입니다."),
    NOT_STUDENT(UNAUTHORIZED, "해당 강의실에 입실한 학생이 아닙니다."),
    BAD_SEARCH_CONDITION(BAD_REQUEST, "검색조건에 문제가 있습니다."),
    NOT_FOUND_TASK(NOT_FOUND, "해당 과제는 존재하지 않습니다."),
    NO_AUTHORIZATION(UNAUTHORIZED, "권한이 없습니다."),
    ALREADY_JOIN(ALREADY_REPORTED, "이미 해당 강의실에 입실하였습니다."),
    NOT_MEMBER(UNAUTHORIZED, "해당 강의실에 입실하지 않았습니다."),
    WRONG_COURSE_NAME(BAD_REQUEST, "강의실 이름을 잘못 입력하셨습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
