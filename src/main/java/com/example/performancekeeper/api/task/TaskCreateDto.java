package com.example.performancekeeper.api.task;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateDto {
    private String name;
    private String desc;
    private LocalDate startAt;

    public static TaskEntity toEntity(TaskCreateDto dto) {
        TaskEntity entity = new TaskEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDesc());
        entity.setStartAt(dto.getStartAt());
        return entity;
    }
}
