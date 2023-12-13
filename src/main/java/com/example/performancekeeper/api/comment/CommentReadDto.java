package com.example.performancekeeper.api.comment;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentReadDto {
    private Long id;
    private String writerName;
    private Long writerId;
    private String content;
    private List<ReplyReadDto> replies;
    private LocalDateTime createdAt;

    public CommentReadDto(Long id, String writerName, Long writerId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.writerName = writerName;
        this.writerId = writerId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static CommentReadDto fromEntity(CommentEntity entity) {
        return new CommentReadDto(entity.getId(),
                entity.getWriter().getNickname(),
                entity.getWriter().getId(),
                entity.getDeletedAt() == null ? entity.getContent() : "삭제된 댓글입니다.",
                entity.getCreatedAt());
    }
}
