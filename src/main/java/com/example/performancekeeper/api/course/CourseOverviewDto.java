package com.example.performancekeeper.api.course;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class CourseOverviewDto {
    private Long id;
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public CourseOverviewDto(Long id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static CourseOverviewDto fromEntity(CourseEntity entity) {
        return new CourseOverviewDto(entity.getId(), entity.getName(), entity.getDescription(), entity.getCreatedAt());
    }
}
