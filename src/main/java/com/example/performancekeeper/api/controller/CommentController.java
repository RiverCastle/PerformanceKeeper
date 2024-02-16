package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.comment.CommentCreateDto;
import com.example.performancekeeper.api.dto.comment.CommentReadDto;
import com.example.performancekeeper.api.dto.comment.ReplyCreateDto;
import com.example.performancekeeper.api.common.PerformanceKeeperFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/assignedTask/{assignedTaskId}/comment")
public class CommentController {
    private final PerformanceKeeperFacade performanceKeeperFacade;
    @PostMapping
    public void addComment(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @PathVariable("assignedTaskId") Long assignedTaskId,
                           @RequestBody CommentCreateDto commentCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.createComment(userId, courseId, assignedTaskId, commentCreateDto);
    }

    @GetMapping
    public List<CommentReadDto> getComments(Authentication authentication,
                                            @PathVariable("courseId") Long courseId,
                                            @PathVariable("assignedTaskId") Long assignedTaskId) {
        Long userId = Long.parseLong(authentication.getName());
        return performanceKeeperFacade.getComments(userId, courseId, assignedTaskId);
    }

    @PostMapping("/{commentId}/reply")
    public void addComment(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @PathVariable("assignedTaskId") Long assignedTaskId,
                           @PathVariable("commentId") Long commentId,
                           @RequestBody ReplyCreateDto replyCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.createReply(userId, courseId, assignedTaskId, commentId, replyCreateDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(Authentication authentication,
                              @PathVariable("courseId") Long courseId,
                              @PathVariable("assignedTaskId") Long assignedTaskId,
                              @PathVariable("commentId") Long commentId) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.deleteComment(userId, courseId, assignedTaskId, commentId);
    }

    @DeleteMapping("/{commentId}/reply/{replyId}")
    public void deleteReply(Authentication authentication,
                            @PathVariable("courseId") Long courseId,
                            @PathVariable("assignedTaskId") Long assignedTaskId,
                            @PathVariable("commentId") Long commentId,
                            @PathVariable("replyId") Long replyId) {
        Long userId = Long.parseLong(authentication.getName());
        performanceKeeperFacade.deleteReply(userId, courseId, assignedTaskId, commentId, replyId);
    }
}
