package com.example.performancekeeper.api.dto.course;

import com.example.performancekeeper.api.entity.CourseEntity;
import lombok.Data;

/**
 * 강의실 생성 요청 dto 클래스
 * 강의실 이름, 참여코드, 소개 내용을 포함
 */
@Data
public class CourseCreateDto {
    private String name;
    private String joinCode;
    private String description;
    public static CourseEntity toEntity(CourseCreateDto dto) {
        CourseEntity entity = new CourseEntity();
        entity.setName(dto.getName());
        entity.setJoinCode(dto.getJoinCode());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
