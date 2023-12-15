package com.example.performancekeeper.api.comment;

import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.task.AssignedTaskEntity;

import java.util.List;

public interface CommentService {
    void createComment(MemberEntity member, AssignedTaskEntity assignedTask, CommentCreateDto commentCreateDto);

    List<CommentReadDto> getComments(MemberEntity member, AssignedTaskEntity assignedTask);

    void createReply(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment, ReplyCreateDto replyCreateDto);

    void deleteComment(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment);

    void deleteReply(Long userId, Long courseId, Long assignedTaskId, Long commentId, Long replyId);

    CommentEntity checkComment(Long commentId);
}
