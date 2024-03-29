package com.example.performancekeeper.api.entity.comment;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private AssignedTaskEntity assignedTaskEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity writer;

    public CommentEntity(String content, AssignedTaskEntity assignedTaskEntity, MemberEntity writer) {
        this.content = content;
        this.assignedTaskEntity = assignedTaskEntity;
        this.writer = writer;
    }
}
