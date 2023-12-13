package com.example.performancekeeper.api.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyReadDto {
    private Long id;
    private String writerName;
    private String content;
    private LocalDateTime createdAt;

    public ReplyReadDto(Long id, String writerName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.writerName = writerName;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static ReplyReadDto fromEntity(ReplyEntity entity) {
        return new ReplyReadDto(entity.getId(),
                entity.getWriter().getNickname(),
                entity.getDeletedAt() == null ? entity.getContent() : "삭제된 답글입니다.",
                entity.getCreatedAt());
    }
}
