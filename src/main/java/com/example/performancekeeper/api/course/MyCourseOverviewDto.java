package com.example.performancekeeper.api.course;

import lombok.Data;

@Data
public class MyCourseOverviewDto {
    private Long id;
    private String name;
    private String role;
    private int progress;

    public MyCourseOverviewDto(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
