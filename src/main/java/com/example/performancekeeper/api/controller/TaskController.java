package com.example.performancekeeper.api.controller;


import com.example.performancekeeper.api.common.PerformanceKeeperFacade;
import com.example.performancekeeper.api.dto.task.AssignedTaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskCreateDto;
import com.example.performancekeeper.api.dto.task.TaskOverviewDto;
import com.example.performancekeeper.api.dto.task.TaskStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/task")
public class TaskController {
    private final PerformanceKeeperFacade performanceKeeperFacade;

    @GetMapping("/{taskId}")
    public TaskOverviewDto getTaskDetails(Authentication authentication,
                                          @PathVariable("courseId") Long courseId,
                                          @PathVariable("taskId") Long assignedTaskId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getTaskDetails(userId, courseId, assignedTaskId);
    }

    @GetMapping(value = "/my-progress", params = "date")// 학생이 자신의 진행상황을 요청
    public List<AssignedTaskOverviewDto>[] getCompletedTasksAndUncompletedTasksByDate(Authentication authentication,
                                                                                      @PathVariable("courseId") Long courseId,
                                                                                      @RequestParam("date") LocalDate date) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.searchCompletedTasksAndUncompletedTasksByDate(userId, courseId, date);
    }

    @GetMapping(value = "/my-progress", params = "keyword")
    public List<AssignedTaskOverviewDto>[] getCompletedTasksAndUncompletedTasksByKeyword(Authentication authentication,
                                                                                         @PathVariable("courseId") Long courseId,
                                                                                         @RequestParam("keyword") String keyword) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.searchCompletedTasksAndUncompletedTasksByKeyword(userId, courseId, keyword);
    }
    @GetMapping("/course-progress")// 매니저가 과제명과 각 학생의 진행상황을 요청
    public Map<String, Object> getTasksProgressesByDate(Authentication authentication,
                                                            @PathVariable("courseId") Long courseId,
                                                            @RequestParam("date") LocalDate startAt) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getTasksAndProgressesByDate(userId, courseId, startAt);
    }

    @GetMapping("/uncompleted-assigned-tasks")
    public List<AssignedTaskOverviewDto> getUncompletedAssignedTasksOfThisCourse(Authentication authentication,
                                                                                 @PathVariable("courseId") Long courseId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getUncompletedAssignedTasksOfThisCourse(userId, courseId);
    }

    @PostMapping
    public void createTask(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @RequestBody TaskCreateDto taskCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.createTask(userId, courseId, taskCreateDto);
    }

    @PutMapping("/{taskId}")
    public void updateTaskStatus(Authentication authentication,
                                 @PathVariable("courseId") Long courseId,
                                 @PathVariable("taskId") Long assignedTaskId,
                                 @RequestBody TaskStatusDto taskStatusDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.updateTaskStatus(userId, courseId, assignedTaskId, taskStatusDto);
    }

    @GetMapping("/{taskId}/progress")
    public int[] getProgressOfThisTask(Authentication authentication,
                                     @PathVariable("courseId") Long courseId,
                                     @PathVariable("taskId") Long taskId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getProgressOfThisTask(userId, courseId, taskId);
    }
}
