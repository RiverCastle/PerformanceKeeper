package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.dto.comment.CommentCreateDto;
import com.example.performancekeeper.api.dto.comment.CommentReadDto;
import com.example.performancekeeper.api.dto.comment.ReplyCreateDto;
import com.example.performancekeeper.api.entity.comment.CommentEntity;
import com.example.performancekeeper.api.entity.comment.ReplyEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;

import java.util.List;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface CommentService {
    void createComment(MemberEntity member, AssignedTaskEntity assignedTask, CommentCreateDto commentCreateDto);

    List<CommentReadDto> getComments(MemberEntity member, AssignedTaskEntity assignedTask);

    void createReply(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment, ReplyCreateDto replyCreateDto);

    void deleteComment(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment);

    void deleteReply(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment, ReplyEntity reply);

    CommentEntity checkComment(Long commentId);

    ReplyEntity checkReply(Long replyId);
}
