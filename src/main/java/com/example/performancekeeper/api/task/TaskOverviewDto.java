package com.example.performancekeeper.api.task;

import lombok.Data;

@Data
public class TaskOverviewDto {
    private Long id;
    private String name;

    public TaskOverviewDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TaskOverviewDto fromEntity(TaskEntity entity) {
        return new TaskOverviewDto(entity.getId(), entity.getName());
    }
}
