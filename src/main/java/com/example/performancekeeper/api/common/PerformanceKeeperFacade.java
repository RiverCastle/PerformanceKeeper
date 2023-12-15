package com.example.performancekeeper.api.common;

import com.example.performancekeeper.api.comment.CommentEntity;
import com.example.performancekeeper.api.comment.CommentService;
import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.course.CourseService;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberService;
import com.example.performancekeeper.api.task.AssignedTaskEntity;
import com.example.performancekeeper.api.task.TaskEntity;
import com.example.performancekeeper.api.task.TaskService;
import com.example.performancekeeper.api.users.UserEntity;
import com.example.performancekeeper.api.users.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PerformanceKeeperFacade {
    private final UserService userService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TaskService taskService;
    private final CommentService commentService;

    public UserEntity checkUser(Long userId) {
        return userService.checkUser(userId);
    }
    public CourseEntity checkCourse(Long courseId) {
        return courseService.checkCourse(courseId);
    }

    public MemberEntity checkMember(UserEntity user, CourseEntity course) {
        return memberService.checkMember(user, course);
    }

    public TaskEntity checkTask(Long taskId) {
        return taskService.checkTask(taskId);
    }

    public AssignedTaskEntity checkAssignedTask(Long assignedTaskId) {
        return taskService.checkAssignedTask(assignedTaskId);
    }

    public CommentEntity checkComment(Long commentId) {
        return commentService.checkComment(commentId);
    }
}
