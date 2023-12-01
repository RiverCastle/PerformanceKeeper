package com.example.performancekeeper.api.task;

import lombok.Data;

@Data
public class AssignedTaskStatusDto {
    private Long id;
    private String status;

    public AssignedTaskStatusDto(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public static AssignedTaskStatusDto fromEntity(AssignedTaskEntity entity) {
        return new AssignedTaskStatusDto(entity.getId(), entity.getStatus());
    }
}
