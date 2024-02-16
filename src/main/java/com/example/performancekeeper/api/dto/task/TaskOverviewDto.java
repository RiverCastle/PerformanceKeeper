package com.example.performancekeeper.api.dto.task;

import com.example.performancekeeper.api.entity.task.TaskEntity;
import lombok.Data;

@Data
public class TaskOverviewDto {
    private Long id;
    private String name;
    private String description;

    public TaskOverviewDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static TaskOverviewDto fromEntity(TaskEntity entity) {
        return new TaskOverviewDto(entity.getId(), entity.getName(), entity.getDescription());
    }
}
