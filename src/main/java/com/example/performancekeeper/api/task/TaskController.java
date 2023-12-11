package com.example.performancekeeper.api.task;


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
    private final TaskService taskService;

    @GetMapping("/{taskId}")
    public TaskOverviewDto getTaskDetails(Authentication authentication,
                                          @PathVariable("courseId") Long courseId,
                                          @PathVariable("taskId") Long assignedTaskId) {
        Long userId = Long.parseLong(authentication.getName());
        return taskService.getTaskDetails(userId, courseId, assignedTaskId);
    }

    @GetMapping("/my-progress")// 학생이 자신의 진행상황을 요청
    public List<AssignedTaskOverviewDto>[] getCompletedTasksAndUncompletedTasksByDate(Authentication authentication,
                                                                                      @PathVariable("courseId") Long courseId,
                                                                                      @RequestParam("date") LocalDate date) {
        Long userId = Long.parseLong(authentication.getName());
        return taskService.searchCompletedTasksAndUncompletedTasksByDate(userId, courseId, date);
    }
    @GetMapping("/course-progress")// 매니저가 과제명과 각 학생의 진행상황을 요청
    public Map<String, Object> getTasksProgressesByDate(Authentication authentication,
                                                            @PathVariable("courseId") Long courseId,
                                                            @RequestParam("date") LocalDate startAt) {
        Long userId = Long.parseLong(authentication.getName());
        return taskService.getTasksAndProgressesByDate(userId, courseId, startAt);
    }
    @PostMapping
    public void createTask(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @RequestBody TaskCreateDto taskCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        taskService.createTask(userId, courseId, taskCreateDto);
    }

    @PutMapping("/{taskId}")
    public void updateTaskStatus(Authentication authentication,
                                 @PathVariable("courseId") Long courseId,
                                 @PathVariable("taskId") Long taskId,
                                 @RequestBody TaskStatusDto taskStatusDto) {
        Long userId = Long.parseLong(authentication.getName());
        taskService.updateTaskStatus(userId, courseId, taskId, taskStatusDto);
    }

    @GetMapping("/{taskId}/progress")
    public int[] getProgressOfThisTask(Authentication authentication,
                                     @PathVariable("courseId") Long courseId,
                                     @PathVariable("taskId") Long taskId) {
        Long userId = Long.parseLong(authentication.getName());
        return taskService.getProgressOfThisTask(userId, courseId, taskId);
    }
}
