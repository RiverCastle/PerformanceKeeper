package com.example.performancekeeper.api.dto.task;

import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import lombok.Data;

/**
 * 부여된 과제의 대략적인 정보를 담은 DTO 클래스입니다.
 */
@Data
public class AssignedTaskOverviewDto {
    /** 부여된 과제의 ID */
    private Long id;
    /** 과제의 이름 */
    private String name;
    /** 과제의 상태 */
    private String status;
    /**
     * 부여된 과제의 대략적인 정보를 생성하는 생성자입니다.
     *
     * @param id 부여된 과제의 ID
     * @param name 과제의 이름
     * @param status 과제의 상태
     */
    public AssignedTaskOverviewDto(Long id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    /**
     * AssignedTaskEntity 객체를 이용하여 AssignedTaskOverviewDto 객체를 생성하는 정적 메서드입니다.
     *
     * @param entity AssignedTaskEntity 객체
     * @return 생성된 AssignedTaskOverviewDto 객체
     */
    public static AssignedTaskOverviewDto fromEntity(AssignedTaskEntity entity) {
        return new AssignedTaskOverviewDto(entity.getId(), entity.getTask().getName(), entity.getStatus());
    }
}
