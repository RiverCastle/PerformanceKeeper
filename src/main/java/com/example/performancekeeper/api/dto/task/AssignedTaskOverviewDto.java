package com.example.performancekeeper.api.dto.task;

import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import lombok.Data;

@Data
public class AssignedTaskOverviewDto {
    private Long id;
    private String name;
    private String status;

    public AssignedTaskOverviewDto(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public static AssignedTaskOverviewDto fromEntity(AssignedTaskEntity entity) {
        return new AssignedTaskOverviewDto(entity.getId(), entity.getTask().getName(), entity.getStatus());
    }
}
