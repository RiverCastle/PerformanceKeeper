package com.example.performancekeeper.api.task;


import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<AssignedTaskOverviewDto>[] getTasksByDate(Authentication authentication,
                                                          @PathVariable("courseId") Long courseId,
                                                          @RequestParam("date") LocalDate date) {
        Long userId = Long.parseLong(authentication.getName());
        return taskService.searchTasksByDate(userId, courseId, date);
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
}
