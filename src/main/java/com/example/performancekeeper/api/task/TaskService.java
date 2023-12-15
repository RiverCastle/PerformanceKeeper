package com.example.performancekeeper.api.task;


import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.member.MemberEntity;
import com.example.performancekeeper.api.member.MemberOverviewDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaskService {
    void assignTasksToNewStudent(CourseEntity course, MemberEntity memberEntity);// 새 학생에게 기존의 과제들을 부여하기
    void createTask(Long userId, Long courseId, TaskCreateDto taskCreateDto);
    int getProgress(MemberEntity member);
    List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByDate(Long userId, Long courseId, LocalDate date); // 학생이 날짜별 자신의 진행상황 조회
    void updateTaskStatus(Long userId, Long courseId, Long taskId, TaskStatusDto taskStatusDto);
    List<Object> getTasksByDate(Long userId, Long courseId, LocalDate startAt);
    Map<MemberOverviewDto, List<AssignedTaskStatusDto>> getProgressesByDate(Long userId, Long courseId, LocalDate startAt);
    Map<String, Object> getTasksAndProgressesByDate(Long userId, Long courseId, LocalDate startAt); // 매니저가 날짜별 과제 진행상황 파악
    void deleteAssignedTasksOfLeavingStudent(MemberEntity member);
    int[] getProgressOfThisTask(Long userId, Long courseId, Long taskId);

    AssignedTaskEntity getAssignedTask(Long assignedTaskId);

    TaskOverviewDto getTaskDetails(Long userId, Long courseId, Long assignedTaskId);

    TaskEntity checkTask(Long taskId);
}
