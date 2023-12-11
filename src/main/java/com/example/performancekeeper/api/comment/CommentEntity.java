package com.example.performancekeeper.api.comment;

import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.task.AssignedTaskEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    private AssignedTaskEntity assignedTaskEntity;
    @ManyToOne
    private MemberEntity writer;

    public CommentEntity(String content, AssignedTaskEntity assignedTaskEntity, MemberEntity writer) {
        this.content = content;
        this.assignedTaskEntity = assignedTaskEntity;
        this.writer = writer;
    }
}
