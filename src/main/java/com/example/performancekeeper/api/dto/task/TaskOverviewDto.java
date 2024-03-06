package com.example.performancekeeper.api.dto.task;

import com.example.performancekeeper.api.entity.task.TaskEntity;
import lombok.Data;

/**
 * 과제의 대략적인 정보를 담은 DTO 클래스입니다.
 */
@Data
public class TaskOverviewDto {
    /** 과제의 ID */
    private Long id;
    /** 과제의 이름 */
    private String name;
    /** 과제의 설명 */
    private String description;

    /**
     * 과제의 대략적인 정보를 생성하는 생성자입니다.
     *
     * @param id 과제의 ID
     * @param name 과제의 이름
     * @param description 과제의 설명
     */
    public TaskOverviewDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * TaskEntity 객체를 이용하여 TaskOverviewDto 객체를 생성하는 정적 메서드입니다.
     *
     * @param entity TaskEntity 객체
     * @return 생성된 TaskOverviewDto 객체
     */
    public static TaskOverviewDto fromEntity(TaskEntity entity) {
        return new TaskOverviewDto(entity.getId(), entity.getName(), entity.getDescription());
    }
}
