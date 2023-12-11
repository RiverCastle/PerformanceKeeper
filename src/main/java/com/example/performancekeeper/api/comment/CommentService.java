package com.example.performancekeeper.api.comment;

public interface CommentService {
    void createComment(Long userId, Long courseId, Long assignedTaskId, CommentCreateDto commentCreateDto);
}
