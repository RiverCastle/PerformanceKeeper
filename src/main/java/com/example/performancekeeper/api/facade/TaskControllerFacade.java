package com.example.performancekeeper.api.facade;

import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import com.example.performancekeeper.api.dto.task.AssignedTaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskCreateDto;
import com.example.performancekeeper.api.dto.task.TaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskStatusDto;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.UserEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;
import com.example.performancekeeper.api.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TaskControllerFacade {
    private final UserService userService;
    private final CourseService courseService;
    private final MemberService memberService;
    private final TaskService taskService;

    public TaskOverviewDto getTaskDetails(Long userId, Long courseId, Long assignedTaskId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkMember(user, course);
        AssignedTaskEntity assignedTask = taskService.checkAssignedTask(assignedTaskId);
        if (!assignedTask.getMember().equals(member) && !member.getRole().equals("Manager")) throw new CustomException(CustomErrorCode.NO_AUTHORIZATION);
        return taskService.getTaskDetails(assignedTask);
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

    public List<AssignedTaskOverviewDto> getUncompletedAssignedTasksOfThisCourse(Long userId, Long courseId) {
        UserEntity user = userService.checkUser(userId);
        CourseEntity course = courseService.checkCourse(courseId);
        MemberEntity member = memberService.checkStudentMember(user, course);
        return taskService.getUncompletedAssignedTasksListOfThisCourse(member);
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
