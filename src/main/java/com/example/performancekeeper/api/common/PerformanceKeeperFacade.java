package com.example.performancekeeper.api.common;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.comment.CommentCreateDto;
import com.example.performancekeeper.api.dto.comment.CommentReadDto;
import com.example.performancekeeper.api.dto.comment.ReplyCreateDto;
import com.example.performancekeeper.api.dto.course.*;
import com.example.performancekeeper.api.dto.member.JoinCourseDto;
import com.example.performancekeeper.api.dto.member.LeaveRequestDto;
import com.example.performancekeeper.api.dto.member.MemberOverviewDto;
import com.example.performancekeeper.api.dto.member.NicknameUpdateDto;
import com.example.performancekeeper.api.dto.task.AssignedTaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskCreateDto;
import com.example.performancekeeper.api.dto.task.TaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskStatusDto;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.comment.CommentEntity;
import com.example.performancekeeper.api.entity.comment.ReplyEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;
import com.example.performancekeeper.api.service.CommentService;
import com.example.performancekeeper.api.service.CourseService;
import com.example.performancekeeper.api.service.MemberService;
import com.example.performancekeeper.api.service.TaskService;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PerformanceKeeperFacade {
    private final UserService userService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TaskService taskService;
    private final CommentService commentService;

    public void createCourse(Long userId, CourseCreateDto courseCreateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.createCourse(courseCreateDto);
        memberService.createManagerMember(user, course);
    }

    public List<CourseOverviewDto> searchCourse(String keyword) {
        return courseService.searchCourse(keyword);
    }

    public List<MyCourseOverviewDto> getMyCourses(Long userId) {
        UserEntity user = userService.checkUser(userId);
        List<MyCourseOverviewDto> myCoursesDtoList = new ArrayList<>();
        List<MemberEntity> myMemberList = memberService.getMyMember(user);
        for (MemberEntity memberEntity : myMemberList) {
            CourseEntity course = memberEntity.getCourse();
            MyCourseOverviewDto myCourseOverviewDto = new MyCourseOverviewDto(course.getId(), course.getName(), memberEntity.getRole());
            myCourseOverviewDto.setProgress(taskService.getProgress(memberEntity));
            myCoursesDtoList.add(myCourseOverviewDto);
        }
        return myCoursesDtoList;
    }

    public CourseDetailsDto getCourseDetails(Long userId, Long courseId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkMember(user, course); // 가입 여부 파악을 위해 할당 필요X
        return courseService.getCourseDetails(course);
    }

    public void updateCourseName(Long userId, Long courseId, CourseNameUpdateDto courseNameUpdateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkManagerMember(user, course);
        courseService.updateCourseName(course, courseNameUpdateDto);
    }

    public void updateCourseDescription(Long userId, Long courseId, CourseDescriptionUpdateDto courseDescriptionUpdateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkManagerMember(user, course);
        courseService.updateCourseDescription(course, courseDescriptionUpdateDto);
    }

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

    @Transactional
    public void createStudentMember(Long userId, Long courseId, JoinCourseDto joinCourseDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.createStudentMember(user, course, joinCourseDto);
        taskService.assignTasksToNewStudent(course, member); // 새 학생에게 기존의 과제물 부여

    }

    @Transactional
    public void deleteStudentMember(Long userId, Long courseId, LeaveRequestDto leaveRequestDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkStudentMemberBeforeDelete(user, course, leaveRequestDto);
        taskService.deleteAssignedTasksOfLeavingStudent(member);
        memberService.deleteStudentMember(member);
    }

    public void changeNickname(Long userId, Long courseId, NicknameUpdateDto nicknameUpdateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        memberService.changeNickname(member, nicknameUpdateDto);
    }

    public MemberOverviewDto getMemberInfo(Long userId, Long courseId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        return memberService.getMemberInfo(member);
    }

    public TaskOverviewDto getTaskDetails(Long userId, Long courseId, Long assignedTaskId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager")) throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        return taskService.getTaskDetails(assignedTask);
    }

    public List<AssignedTaskOverviewDto> getUncompletedAssignedTasksOfThisCourse(Long userId, Long courseId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkStudentMember(user, course);
        return taskService.getUncompletedAssignedTasksListOfThisCourse(member);
    }

    public List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByDate(Long userId, Long courseId, LocalDate date) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        return taskService.searchCompletedTasksAndUncompletedTasksByDate(member, date);
    }

    public List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByKeyword(Long userId, Long courseId, String keyword) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        return taskService.searchCompletedTasksAndUncompletedTasksByKeyword(member, keyword);
    }

    public Map<String, Object> getTasksAndProgressesByDate(Long userId, Long courseId, LocalDate startAt) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkManagerMember(user, course);
        List<MemberEntity> studentsOfThisCourse = memberService.getAllStudentsOfThisCourse(course);
        return taskService.getTasksAndProgressesByDate(course, studentsOfThisCourse, startAt);
    }

    @Transactional
    public void createTask(Long userId, Long courseId, TaskCreateDto taskCreateDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkManagerMember(user, course);
        List<MemberEntity> studentsOfThisCourse = memberService.getAllStudentsOfThisCourse(course);
        taskService.createTask(course, taskCreateDto, studentsOfThisCourse);
    }

    public void updateTaskStatus(Long userId, Long courseId, Long assignedTaskId, TaskStatusDto taskStatusDto) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity student = memberService.checkStudentMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        if (!assignedTask.getMember().equals(student)) throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        taskService.updateTaskStatus(assignedTask, taskStatusDto);
    }

    public int[] getProgressOfThisTask(Long userId, Long courseId, Long taskId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        memberService.checkManagerMember(user, course);
        TaskEntity task = taskService.checkTask(taskId);
        return taskService.getProgressOfThisTask(task);
    }
}
