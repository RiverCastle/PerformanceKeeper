package com.example.performancekeeper.api.facade;

import com.example.performancekeeper.api.dto.comment.CommentCreateDto;
import com.example.performancekeeper.api.dto.comment.CommentReadDto;
import com.example.performancekeeper.api.dto.comment.ReplyCreateDto;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.comment.CommentEntity;
import com.example.performancekeeper.api.entity.comment.ReplyEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import com.example.performancekeeper.api.service.CommentService;
import com.example.performancekeeper.api.service.CourseService;
import com.example.performancekeeper.api.service.MemberService;
import com.example.performancekeeper.api.service.TaskService;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentControllerFacade {
    private final UserService userService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TaskService taskService;
    private final CommentService commentService;

    public void createComment(Long userId, Long courseId, Long assignedTaskId, CommentCreateDto commentCreateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        commentService.createComment(member, assignedTask, commentCreateDto);
    }

    public List<CommentReadDto> getComments(Long userId, Long courseId, Long assignedTaskId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);

        return commentService.getComments(member, assignedTask);
    }

    public void createReply(Long userId, Long courseId, Long assignedTaskId, Long commentId, ReplyCreateDto replyCreateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        CommentEntity comment = commentService.checkComment(commentId);
        commentService.createReply(member, assignedTask, comment, replyCreateDto);
    }

    public void deleteComment(Long userId, Long courseId, Long assignedTaskId, Long commentId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        CommentEntity comment = commentService.checkComment(commentId);
        commentService.deleteComment(member, assignedTask, comment);
    }

    public void deleteReply(Long userId, Long courseId, Long assignedTaskId, Long commentId, Long replyId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        CommentEntity comment = commentService.checkComment(commentId);
        ReplyEntity reply = commentService.checkReply(replyId);
        commentService.deleteReply(member, assignedTask, comment, reply);
    }























}
