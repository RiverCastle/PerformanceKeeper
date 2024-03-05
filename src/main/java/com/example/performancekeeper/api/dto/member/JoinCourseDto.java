package com.example.performancekeeper.api.dto.member;

import lombok.Data;

/**
 * 유저가 강의실에 입실할 때 필요한 참여코드가 담기는 Dto 클래스
 */
@Data
public class JoinCourseDto {
    private String joinCode;
}
