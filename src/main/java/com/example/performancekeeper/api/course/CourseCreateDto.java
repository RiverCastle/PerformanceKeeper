package com.example.performancekeeper.api.course;

import lombok.Data;

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
