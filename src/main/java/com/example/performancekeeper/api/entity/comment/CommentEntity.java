package com.example.performancekeeper.api.entity.comment;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "UPDATE comment_entity SET deleted_at = NOW() WHERE id = ?")
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
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private final List<ReplyEntity> replies = new ArrayList<>();

    public CommentEntity(String content, AssignedTaskEntity assignedTaskEntity, MemberEntity writer) {
        this.content = content;
        this.assignedTaskEntity = assignedTaskEntity;
        this.writer = writer;
    }
}
