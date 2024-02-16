package com.example.performancekeeper.api.dto.task;

import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateDto {
    private String name;
    private String description;
    private LocalDate startAt;

    public static TaskEntity toEntity(TaskCreateDto dto, CourseEntity course) {
        TaskEntity entity = new TaskEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStartAt(dto.getStartAt());
        entity.setCourse(course);
        return entity;
    }
}
