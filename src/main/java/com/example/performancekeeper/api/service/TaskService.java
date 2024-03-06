package com.example.performancekeeper.api.service;

import com.example.performancekeeper.api.dto.task.AssignedTaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskCreateDto;
import com.example.performancekeeper.api.dto.task.TaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskStatusDto;
import com.example.performancekeeper.api.entity.CourseEntity;
import com.example.performancekeeper.api.entity.MemberEntity;
import com.example.performancekeeper.api.entity.task.AssignedTaskEntity;
import com.example.performancekeeper.api.entity.task.TaskEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 과제 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface TaskService {
    /**
     * 새 학생에게 기존의 과제들을 부여하는 메서드입니다.
     *
     * @param course 과제가 속한 강의실 엔티티
     * @param memberEntity 새 학생 엔티티
     */
    void assignTasksToNewStudent(CourseEntity course, MemberEntity memberEntity);

    /**
     * 과제를 생성하는 메서드입니다.
     *
     * @param course 과제가 속한 강의실 엔티티
     * @param taskCreateDto 생성할 과제 정보를 담은 DTO 객체
     * @param studentsOfThisCourse 이 강의실에 속한 학생 엔티티 리스트
     */
    void createTask(CourseEntity course, TaskCreateDto taskCreateDto, List<MemberEntity> studentsOfThisCourse);

    /**
     * 학생의 진행상황을 조회하는 메서드입니다.
     *
     * @param member 학생 엔티티
     * @return 학생의 진행상황을 나타내는 정수 값
     */
    int getProgress(MemberEntity member);

    /**
     * 학생이 날짜별 자신의 진행상황을 조회하는 메서드입니다.
     *
     * @param member 학생 엔티티
     * @param date 조회할 날짜
     * @return 날짜별 완료 및 미완료된 과제 리스트 배열
     */
    List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByDate(MemberEntity member, LocalDate date);

    /**
     * 과제 상태를 업데이트하는 메서드입니다.
     *
     * @param assignedTask 부여된 과제 엔티티
     * @param taskStatusDto 업데이트할 과제 상태 정보를 담은 DTO 객체
     */
    void updateTaskStatus(AssignedTaskEntity assignedTask, TaskStatusDto taskStatusDto);

    /**
     * 매니저가 날짜별 과제 진행상황을 파악하는 메서드입니다.
     *
     * @param course 과제가 속한 강의실 엔티티
     * @param studentsOfThisCourse 이 강의실에 속한 학생 엔티티 리스트
     * @param startAt 조회할 시작 날짜
     * @return 과제명과 각 학생의 진행 상황을 포함한 맵
     */
    Map<String, Object> getTasksAndProgressesByDate(CourseEntity course, List<MemberEntity> studentsOfThisCourse, LocalDate startAt);

    /**
     * 학생이 탈퇴할 경우 해당 학생의 부여된 과제를 삭제하는 메서드입니다.
     *
     * @param member 탈퇴하는 학생 엔티티
     */
    void deleteAssignedTasksOfLeavingStudent(MemberEntity member);

    /**
     * 특정 과제의 진행 상황을 조회하는 메서드입니다.
     *
     * @param task 과제 엔티티
     * @return 과제의 진행 상황을 나타내는 정수 배열
     */
    int[] getProgressOfThisTask(TaskEntity task);

    /**
     * 부여된 과제 엔티티를 확인하는 메서드입니다.
     *
     * @param assignedTaskId 부여된 과제의 ID
     * @return 해당 ID에 해당하는 부여된 과제 엔티티
     */
    AssignedTaskEntity checkAssignedTask(Long assignedTaskId);

    /**
     * 과제의 상세 정보를 조회하는 메서드입니다.
     *
     * @param assignedTask 부여된 과제 엔티티
     * @return 과제의 상세 정보를 담은 DTO 객체
     */
    TaskOverviewDto getTaskDetails(AssignedTaskEntity assignedTask);

    /**
     * 과제 엔티티를 확인하는 메서드입니다.
     *
     * @param taskId 과제의 ID
     * @return 해당 ID에 해당하는 과제 엔티티
     */
    TaskEntity checkTask(Long taskId);

    /**
     * 이 강의실에 속한 미완료된 부여된 과제 리스트를 조회하는 메서드입니다.
     *
     * @param member 이 강의실에 속한 학생 엔티티
     * @return 미완료된 부여된 과제 리스트
     */
    List<AssignedTaskOverviewDto> getUncompletedAssignedTasksListOfThisCourse(MemberEntity member);

    /**
     * 학생이 특정 키워드를 포함하는 과제를 검색하는 메서드입니다.
     *
     * @param member 학생 엔티티
     * @param keyword 검색할 키워드
     * @return 완료 및 미완료된 과제 리스트 배열
     */
    List<AssignedTaskOverviewDto>[] searchCompletedTasksAndUncompletedTasksByKeyword(MemberEntity member, String keyword);
}

