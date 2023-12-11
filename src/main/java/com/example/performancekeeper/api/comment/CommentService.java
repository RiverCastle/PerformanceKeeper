package com.example.performancekeeper.api.comment;

import java.util.List;

public interface CommentService {
    void createComment(Long userId, Long courseId, Long assignedTaskId, CommentCreateDto commentCreateDto);

    List<CommentReadDto> getComments(Long userId, Long courseId, Long assignedTaskId);
}
