package com.example.performancekeeper.api.course;

import lombok.Data;

@Data
public class CourseCreateDto {
    private String name;
    private String joinCode;
    private String desc;

    public static CourseEntity toEntity(CourseCreateDto dto) {
        CourseEntity entity = new CourseEntity();
        entity.setName(dto.getName());
        entity.setJoinCode(dto.getJoinCode());
        entity.setDesc(dto.getDesc());
        return entity;
    }
}
