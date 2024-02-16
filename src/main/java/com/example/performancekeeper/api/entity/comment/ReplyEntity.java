package com.example.performancekeeper.api.entity.comment;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
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
    private MemberEntity writer;

    public ReplyEntity(String content, CommentEntity comment, MemberEntity writer) {
        this.content = content;
        this.comment = comment;
        this.writer = writer;
    }
}
