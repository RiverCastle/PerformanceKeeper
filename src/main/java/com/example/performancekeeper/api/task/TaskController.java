package com.example.performancekeeper.api.task;


import com.example.performancekeeper.api.common.exception.CustomErrorCode;
import com.example.performancekeeper.api.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<AssignedTaskOverviewDto>[] getTasksByKeyword(Authentication authentication,
                                                             @PathVariable("courseId") Long courseId,
                                                             @RequestBody TaskSearchDto taskSearchDto) {
        Long userId = Long.parseLong(authentication.getName());
        if (taskSearchDto.getCondition().equals("keyword")) return taskService.searchTasksByKeyword(userId, courseId, taskSearchDto.getKeyword());
        else if (taskSearchDto.getCondition().equals("date")) return taskService.searchTasksByDate(userId, courseId, taskSearchDto.getDate());
        else throw new CustomException(CustomErrorCode.BAD_SEARCH_CONDITION);
    }
    @PostMapping
    public void createTask(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @RequestBody TaskCreateDto taskCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        taskService.createTask(userId, courseId, taskCreateDto);
    }
}
