package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.repository.CommentRepository;
import com.example.performancekeeper.api.repository.ReplyRepository;
import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.comment.CommentCreateDto;
import com.example.performancekeeper.api.dto.comment.CommentReadDto;
import com.example.performancekeeper.api.dto.comment.ReplyCreateDto;
import com.example.performancekeeper.api.dto.comment.ReplyReadDto;
import com.example.performancekeeper.api.entity.comment.CommentEntity;
import com.example.performancekeeper.api.entity.comment.ReplyEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 인터페이스 CommentService의 구현체 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Override
    public void createComment(MemberEntity member, AssignedTaskEntity assignedTask, CommentCreateDto commentCreateDto) {
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION); // 작성자 본인 또는 강사만 작성 가능
        commentRepository.save(new CommentEntity(commentCreateDto.getContent(), assignedTask, member));
    }

    @Deprecated
    public List<CommentReadDto> getCommentsDeprecated(MemberEntity member, AssignedTaskEntity assignedTask) {
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION); // 작성자 본인 또는 강사만 작성 가능

        List<CommentReadDto> result = new ArrayList<>();
        List<CommentEntity> commentEntityList = commentRepository.findAllByAssignedTaskEntity(assignedTask);
        for (CommentEntity comment : commentEntityList) {
            CommentReadDto commentReadDto = CommentReadDto.fromEntity(comment);
            List<ReplyEntity> replyEntityList = replyRepository.findAllByComment(comment);
            List<ReplyReadDto> replyReadDtoList = new ArrayList<>();
            for (ReplyEntity reply : replyEntityList) replyReadDtoList.add(ReplyReadDto.fromEntity(reply));
            commentReadDto.setReplies(replyReadDtoList);
            result.add(commentReadDto);
        }
        return result;
    }

    @Override
    public List<CommentReadDto> getComments(MemberEntity member, AssignedTaskEntity assignedTask) {
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION); // 작성자 본인 또는 강사만 작성 가능

        List<CommentReadDto> result = new ArrayList<>();
        List<CommentEntity> comments = commentRepository.findAllByAssignedTaskEntityWithReplies(assignedTask);
        for (CommentEntity comment : comments) {
            result.add(CommentReadDto.fromEntity(comment));
        }
        return result;
    }

    @Override
    public void createReply(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment, ReplyCreateDto replyCreateDto) {
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        replyRepository.save(new ReplyEntity(replyCreateDto.getContent(), assignedTask, comment, member));
    }

    @Override
    public void deleteComment(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment) {
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        if (!comment.getAssignedTaskEntity().equals(assignedTask))
            throw new CustomException(CustomErrorCode.ASSIGNEDTASK_COMMENT_MISMATCH);
        if (!comment.getWriter().equals(member))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        if (comment.getDeletedAt() != null)
            throw new CustomException(CustomErrorCode.ALREADY_DELETED);

        commentRepository.delete(comment);

    }

    @Override
    public void deleteReply(MemberEntity member, AssignedTaskEntity assignedTask, CommentEntity comment, ReplyEntity reply) {
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        if (!reply.getComment().equals(comment))
            throw new CustomException(CustomErrorCode.COMMENT_REPLY_MISMATCH);
        if (reply.getDeletedAt() != null)
            throw new CustomException(CustomErrorCode.ALREADY_DELETED);

        replyRepository.delete(reply);
    }

    @Override
    public CommentEntity checkComment(Long commentId) {
        return commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
    }

    @Override
    public ReplyEntity checkReply(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_REPLY));
    }
}
