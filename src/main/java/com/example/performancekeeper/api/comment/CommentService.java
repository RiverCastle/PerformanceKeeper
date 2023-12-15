package com.example.performancekeeper.api.comment;

import com.example.performancekeeper.api.course.CourseEntity;

import java.util.List;

public interface CommentService {
    void createComment(Long userId, Long courseId, Long assignedTaskId, CommentCreateDto commentCreateDto);

    List<CommentReadDto> getComments(Long userId, Long courseId, Long assignedTaskId);

    void createReply(Long userId, Long courseId, Long assignedTaskId, Long commentId, ReplyCreateDto replyCreateDto);

    void deleteComment(Long userId, Long courseId, Long assignedTaskId, Long commentId);

    void deleteReply(Long userId, Long courseId, Long assignedTaskId, Long commentId, Long replyId);

    CommentEntity checkComment(Long commentId);
}
