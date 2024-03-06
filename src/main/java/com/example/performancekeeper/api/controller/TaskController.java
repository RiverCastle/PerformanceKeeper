package com.example.performancekeeper.api.controller;


import com.example.performancekeeper.api.dto.task.AssignedTaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskCreateDto;
import com.example.performancekeeper.api.dto.task.TaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskStatusDto;
import com.example.performancekeeper.api.facade.TaskControllerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 강의실에 생성된 과제와 관련된 요청을 처리하는 Controller입니다.
 * 과제 생성, 조회, 수정 요청을 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/task")
public class TaskController {
    private final TaskControllerFacade performanceKeeperFacade;

    /**
     * 단일 과제 조회 요청을 처리하는 메서드입니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @param assignedTaskId 조회할 과제의 ID
     * @return 조회 성공 시, 사용자에게 공개 가능한 대략적인 정보를 Dto로 반환합니다.
     */
    @GetMapping("/{taskId}")
    public TaskOverviewDto getTaskDetails(Authentication authentication,
                                          @PathVariable("courseId") Long courseId,
                                          @PathVariable("taskId") Long assignedTaskId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getTaskDetails(userId, courseId, assignedTaskId);
    }

    /**
     * 특정 날짜에 부여된 과제에 대하여 조회 요청을 처리하는 메서드입니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @param date 조회할 날짜
     * @return 사용자에게 공개 가능한 대략적인 과제 리스트를 반환합니다.
     */
    @GetMapping(value = "/my-progress", params = "date")
    public List<AssignedTaskOverviewDto>[] getCompletedTasksAndUncompletedTasksByDate(Authentication authentication,
                                                                                      @PathVariable("courseId") Long courseId,
                                                                                      @RequestParam("date") LocalDate date) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.searchCompletedTasksAndUncompletedTasksByDate(userId, courseId, date);
    }

    /**
     * 제목에 키워드를 포함하는 과제에 대하여 조회 요청을 처리하는 메서드입니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @param keyword 검색할 키워드
     * @return 사용자에게 공개 가능한 대략적인 과제 리스트를 반환합니다.
     */
    @GetMapping(value = "/my-progress", params = "keyword")
    public List<AssignedTaskOverviewDto>[] getCompletedTasksAndUncompletedTasksByKeyword(Authentication authentication,
                                                                                         @PathVariable("courseId") Long courseId,
                                                                                         @RequestParam("keyword") String keyword) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.searchCompletedTasksAndUncompletedTasksByKeyword(userId, courseId, keyword);
    }

    /**
     * 날짜 별 전체 과제 진행 상황에 대한 조회 요청을 처리하는 메서드입니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @param startAt 조회할 시작 날짜
     * @return 과제명과 각 학생의 진행 상황을 포함한 맵을 반환합니다.
     */
    @GetMapping("/course-progress")// 매니저가 과제명과 각 학생의 진행상황을 요청
    public Map<String, Object> getTasksProgressesByDate(Authentication authentication,
                                                            @PathVariable("courseId") Long courseId,
                                                            @RequestParam("date") LocalDate startAt) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getTasksAndProgressesByDate(userId, courseId, startAt);
    }

    /**
     * 강의실의 미완료된 과제를 조회하는 메서드입니다.
     *
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @return 미완료된 과제 리스트를 반환합니다.
     */
    @GetMapping("/uncompleted-assigned-tasks")
    public List<AssignedTaskOverviewDto> getUncompletedAssignedTasksOfThisCourse(Authentication authentication,
                                                                                 @PathVariable("courseId") Long courseId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getUncompletedAssignedTasksOfThisCourse(userId, courseId);
    }

    /**
     * 과제를 생성하는 메서드입니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @param taskCreateDto 생성할 과제의 정보를 담은 DTO 객체
     */
    @PostMapping
    public void createTask(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @RequestBody TaskCreateDto taskCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.createTask(userId, courseId, taskCreateDto);
    }

    /**
     * 과제 상태를 업데이트하는 메서드입니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @param assignedTaskId 업데이트할 과제의 ID
     * @param taskStatusDto 업데이트할 과제 상태 정보를 담은 DTO 객체
     */
    @PutMapping("/{taskId}")
    public void updateTaskStatus(Authentication authentication,
                                 @PathVariable("courseId") Long courseId,
                                 @PathVariable("taskId") Long assignedTaskId,
                                 @RequestBody TaskStatusDto taskStatusDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.updateTaskStatus(userId, courseId, assignedTaskId, taskStatusDto);
    }

    /**
     * 해당 과제의 진행 상황을 조회하는 메서드입니다.
     *
     * @param authentication 현재 사용자의 인증 정보
     * @param courseId 과제가 속한 과정의 ID
     * @param taskId 조회할 과제의 ID
     * @return 과제의 진행 상황을 배열로 반환합니다.
     */
    @GetMapping("/{taskId}/progress")
    public int[] getProgressOfThisTask(Authentication authentication,
                                     @PathVariable("courseId") Long courseId,
                                     @PathVariable("taskId") Long taskId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getProgressOfThisTask(userId, courseId, taskId);
    }
}
