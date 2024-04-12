package com.example.performancekeeper.api.entity.comment;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE reply_entity SET deleted_at = NOW() WHERE id = ?")
@NoArgsConstructor
@Getter
public class ReplyEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity comment;
    @ManyToOne(fetch = FetchType.LAZY)
    private AssignedTaskEntity assignedTaskEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity writer;

    public ReplyEntity(String content, AssignedTaskEntity assignedTaskEntity, CommentEntity comment, MemberEntity writer) {
        this.assignedTaskEntity = assignedTaskEntity;
        this.content = content;
        this.comment = comment;
        this.writer = writer;
    }
}
