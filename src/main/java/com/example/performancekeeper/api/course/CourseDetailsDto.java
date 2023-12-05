package com.example.performancekeeper.api.course;

import lombok.Data;

@Data
public class CourseDetailsDto {
    private Long id;
    private String name;
    private String description;

    public CourseDetailsDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static CourseDetailsDto fromEntity(CourseEntity entity) {
        return new CourseDetailsDto(entity.getId(), entity.getName(), entity.getDescription());
    }
}
