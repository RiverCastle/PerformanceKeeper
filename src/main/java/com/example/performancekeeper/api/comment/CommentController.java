package com.example.performancekeeper.api.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/assignedTaskId/{assignedTaskId}/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    public void addComment(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @PathVariable("assignedTaskId") Long assignedTaskId,
                           @RequestBody CommentCreateDto commentCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.createComment(userId, courseId, assignedTaskId, commentCreateDto);
    }
}
