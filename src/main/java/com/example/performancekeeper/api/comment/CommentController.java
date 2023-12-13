package com.example.performancekeeper.api.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/assignedTask/{assignedTaskId}/comment")
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

    @GetMapping
    public List<CommentReadDto> getComments(Authentication authentication,
                                            @PathVariable("courseId") Long courseId,
                                            @PathVariable("assignedTaskId") Long assignedTaskId) {
        Long userId = Long.parseLong(authentication.getName());
        return commentService.getComments(userId, courseId, assignedTaskId);
    }

    @PostMapping("/{commentId}/reply")
    public void addComment(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @PathVariable("assignedTaskId") Long assignedTaskId,
                           @PathVariable("commentId") Long commentId,
                           @RequestBody ReplyCreateDto replyCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.createReply(userId, courseId, assignedTaskId, commentId, replyCreateDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(Authentication authentication,
                              @PathVariable("courseId") Long courseId,
                              @PathVariable("assignedTaskId") Long assignedTaskId,
                              @PathVariable("commentId") Long commentId) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.deleteComment(userId, courseId, assignedTaskId, commentId);
    }

    @DeleteMapping("/{commentId}/reply/{replyId}")
    public void deleteReply(Authentication authentication,
                            @PathVariable("courseId") Long courseId,
                            @PathVariable("assignedTaskId") Long assignedTaskId,
                            @PathVariable("commentId") Long commentId,
                            @PathVariable("replyId") Long replyId) {
        Long userId = Long.parseLong(authentication.getName());
        commentService.deleteReply(userId, courseId, assignedTaskId, commentId, replyId);

    }
}
