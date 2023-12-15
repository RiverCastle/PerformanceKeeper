package com.example.performancekeeper.api.comment;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseServiceImpl;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberServiceImpl;
import com.example.performancekeeper.api.task.AssignedTaskEntity;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final UserServiceImpl userServiceImpl;
    private final CourseServiceImpl courseServiceImpl;
    private final MemberServiceImpl memberServiceImpl;
    private final TaskService taskService;

    @Override
    public void createComment(MemberEntity member, AssignedTaskEntity assignedTask, CommentCreateDto commentCreateDto) {
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION); // 작성자 본인 또는 강사만 작성 가능
        commentRepository.save(new CommentEntity(commentCreateDto.getContent(), assignedTask, member));
    }

    @Override
    public List<CommentReadDto> getComments(MemberEntity member, AssignedTaskEntity assignedTask) {
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
    public void createReply(Long userId, Long courseId, Long assignedTaskId, Long commentId, ReplyCreateDto replyCreateDto) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
        replyRepository.save(new ReplyEntity(replyCreateDto.getContent(), comment, member));
    }

    @Override
    public void deleteComment(Long userId, Long courseId, Long assignedTaskId, Long commentId) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
        if (!comment.getWriter().equals(member)) throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        if (comment.getDeletedAt() != null) throw new CustomException(CustomErrorCode.ALREADY_DELETED);

        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);

    }

    @Override
    public void deleteReply(Long userId, Long courseId, Long assignedTaskId, Long commentId, Long replyId) {
        UserEntity user = userServiceImpl.checkUser(userId);
        CourseEntity course = courseServiceImpl.checkCourseEntity(courseId);
        MemberEntity member = memberServiceImpl.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager"))
            throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
        ReplyEntity reply = replyRepository.findById(replyId).orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_REPLY));
        if (!reply.getComment().equals(comment)) throw new CustomException(CustomErrorCode.COMMENT_REPLY_MISMATCH);
        if (reply.getDeletedAt() != null) throw new CustomException(CustomErrorCode.ALREADY_DELETED);

        reply.setDeletedAt(LocalDateTime.now());
        replyRepository.save(reply);
    }

    @Override
    public CommentEntity checkComment(Long commentId) {
        return commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NOT_FOUND_COMMENT));
    }
}
