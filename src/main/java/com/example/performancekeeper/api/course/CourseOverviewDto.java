package com.example.performancekeeper.api.course;

import lombok.Data;
import org.springframework.boot.convert.DurationFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.Year;

@Data
public class CourseOverviewDto {
    private Long id;
    private String name;
    private String desc;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public CourseOverviewDto(Long id, String name, String desc, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.createdAt = createdAt;
    }

    public static CourseOverviewDto fromEntity(CourseEntity entity) {
        return new CourseOverviewDto(entity.getId(), entity.getName(), entity.getDesc(), entity.getCreatedAt());
    }
}
