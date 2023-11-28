package com.example.performancekeeper.api.task;

import lombok.Data;

@Data
public class TaskCreateDto {
    private String name;
    private String desc;

    public static TaskEntity toEntity(TaskCreateDto dto) {
        TaskEntity entity = new TaskEntity();
        entity.setName(dto.getName());
        entity.setDesc(dto.getDesc());
        return entity;
    }
}
