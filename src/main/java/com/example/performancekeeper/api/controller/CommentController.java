package com.example.performancekeeper.api.controller;

import com.example.performancekeeper.api.dto.comment.CommentCreateDto;
import com.example.performancekeeper.api.dto.comment.CommentReadDto;
import com.example.performancekeeper.api.dto.comment.ReplyCreateDto;
import com.example.performancekeeper.api.facade.CommentControllerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글/답글 관련 요청을 처리하는 Controller입니다.
 * 댓글/답글 등록, 조회, 삭제 요청을 처리합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/{courseId}/assignedTask/{assignedTaskId}/comment")
public class CommentController {
    private final CommentControllerFacade commentControllerFacade;
    @PostMapping
    public void addComment(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @PathVariable("assignedTaskId") Long assignedTaskId,
                           @RequestBody CommentCreateDto commentCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        commentControllerFacade.createComment(userId, courseId, assignedTaskId, commentCreateDto);
    }

    @GetMapping
    public List<CommentReadDto> getComments(Authentication authentication,
                                            @PathVariable("courseId") Long courseId,
                                            @PathVariable("assignedTaskId") Long assignedTaskId) {
        Long userId = Long.parseLong(authentication.getName());
        return commentControllerFacade.getComments(userId, courseId, assignedTaskId);
    }

    @PostMapping("/{commentId}/reply")
    public void addComment(Authentication authentication,
                           @PathVariable("courseId") Long courseId,
                           @PathVariable("assignedTaskId") Long assignedTaskId,
                           @PathVariable("commentId") Long commentId,
                           @RequestBody ReplyCreateDto replyCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        commentControllerFacade.createReply(userId, courseId, assignedTaskId, commentId, replyCreateDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(Authentication authentication,
                              @PathVariable("courseId") Long courseId,
                              @PathVariable("assignedTaskId") Long assignedTaskId,
                              @PathVariable("commentId") Long commentId) {
        Long userId = Long.parseLong(authentication.getName());
        commentControllerFacade.deleteComment(userId, courseId, assignedTaskId, commentId);
    }

    @DeleteMapping("/{commentId}/reply/{replyId}")
    public void deleteReply(Authentication authentication,
                            @PathVariable("courseId") Long courseId,
                            @PathVariable("assignedTaskId") Long assignedTaskId,
                            @PathVariable("commentId") Long commentId,
                            @PathVariable("replyId") Long replyId) {
        Long userId = Long.parseLong(authentication.getName());
        commentControllerFacade.deleteReply(userId, courseId, assignedTaskId, commentId, replyId);
    }
}
