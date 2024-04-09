package com.example.performancekeeper.api.dto.member;

import lombok.Data;

/**
 * 강의실 퇴실 (멤버 삭제) 의사를 담는 Dto 객체
 * 퇴실 의사를 CourseName 입력을 통해 확인
 * 일치할 경우 퇴실 | 일치하지 않을 경우 무효한 요청
 */
@Data
public class LeaveRequestDto {
    private String courseNameCheck;

    public String getCourseNameCheck() {
        return courseNameCheck;
    }
}
