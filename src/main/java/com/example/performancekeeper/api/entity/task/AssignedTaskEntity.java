package com.example.performancekeeper.api.entity.task;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.comment.CommentEntity;
import com.example.performancekeeper.api.entity.comment.ReplyEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "assignedTaskEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments = new ArrayList<>();
    @OneToMany(mappedBy = "assignedTaskEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReplyEntity> replies = new ArrayList<>();
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
