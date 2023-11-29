package com.example.performancekeeper.api.task;

import com.example.performancekeeper.api.member.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssignedTaskEntity extends TaskEntity {
    @ManyToOne
    private MemberEntity member;
    private String status;



    public static AssignedTaskEntity fromTaskEntity(TaskEntity taskEntity) {
        AssignedTaskEntity assignedTaskEntity = new AssignedTaskEntity();
        assignedTaskEntity.setName(taskEntity.getName());
        assignedTaskEntity.setDesc(taskEntity.getDesc());
        assignedTaskEntity.setStartAt(taskEntity.getStartAt());
        assignedTaskEntity.setCourse(taskEntity.getCourse());
        assignedTaskEntity.setStatus("등록");
        return assignedTaskEntity;
    }
}
