package com.example.performancekeeper.api.entity.task;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE assigned_task_entity SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor
public class AssignedTaskEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TaskEntity task;
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;
    private String status;

    public AssignedTaskEntity(TaskEntity task, MemberEntity member, String status) {
        this.task = task;
        this.member = member;
        this.status = status;
    }


    public static AssignedTaskEntity fromTaskEntity(TaskEntity entity, MemberEntity member) {
        return new AssignedTaskEntity(entity, member, "등록");
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }
}
