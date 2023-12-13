package com.example.performancekeeper.api.comment;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import com.example.performancekeeper.api.member.MemberEntity;
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
    @ManyToOne
    private CommentEntity comment;
    @ManyToOne
    private MemberEntity writer;

    public ReplyEntity(String content, CommentEntity comment, MemberEntity writer) {
        this.content = content;
        this.comment = comment;
        this.writer = writer;
    }
}
