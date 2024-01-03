package com.example.performancekeeper.api.task;


import com.example.performancekeeper.api.course.CourseEntity;
import com.example.performancekeeper.api.member.MemberEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaskService {
    void assignTasksToNewStudent(CourseEntity course, MemberEntity memberEntity);// 새 학생에게 기존의 과제들을 부여하기
    void createTask(CourseEntity course, TaskCreateDto taskCreateDto, List<MemberEntity> studentsOfThisCourse);
    int getProgress(MemberEntity member);
    List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByDate(MemberEntity member, LocalDate date); // 학생이 날짜별 자신의 진행상황 조회
    void updateTaskStatus(AssignedTaskEntity assignedTask, TaskStatusDto taskStatusDto);
    Map<String, Object> getTasksAndProgressesByDate(CourseEntity course, List<MemberEntity> studentsOfThisCourse, LocalDate startAt); // 매니저가 날짜별 과제 진행상황 파악
    void deleteAssignedTasksOfLeavingStudent(MemberEntity member);
    int[] getProgressOfThisTask(TaskEntity task);

    AssignedTaskEntity checkAssignedTask(Long assignedTaskId);

    TaskOverviewDto getTaskDetails(AssignedTaskEntity assignedTask);

    TaskEntity checkTask(Long taskId);

    List<AssignedTaskOverviewDto> getUncompletedAssignedTasksListOfThisCourse(MemberEntity member);

    List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByKeyword(MemberEntity member, String keyword);
}
